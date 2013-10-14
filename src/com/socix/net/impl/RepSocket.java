package com.socix.net.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.socix.logging.LogManager;
import com.socix.net.spi.ISocket;
import com.socix.net.spi.ISocketListener;
import com.socix.util.FormatConverter;
import com.socix.util.IDGenerater;

public class RepSocket extends Thread implements ISocket {
	private static Logger log = LogManager.getLogger(RepSocket.class.getName());
	private Socket socket;
	private String key;
	private boolean enable = true;
	private List<ISocketListener> listener = new ArrayList<ISocketListener>();
	private InputStream is;
	private OutputStream os;
	private byte[] head = new byte[17];
	private byte[] rhead = new byte[17];
	private byte[] body;
	private byte[] rbody;
	
	public RepSocket() {
		this.socket = new Socket();
	}
	
	public RepSocket(Socket socket) {
		this.socket = socket;
		try {
			this.is = this.socket.getInputStream();
			this.os = this.socket.getOutputStream();
		} catch(IOException e) {
			log.error("RepSocket:", e);
		}
		this.key = String.valueOf(IDGenerater.generateRequestID());
		this.start();
	}
	
	public boolean connect(SocketAddress endpoint) {
		try {
			this.socket.connect(endpoint);
			this.is = this.socket.getInputStream();
			this.os = this.socket.getOutputStream();
			this.start();
			return true;
		} catch (IOException e) {
			log.error("RepSocket:", e);
			return false;
		}
	}	
	
	public String getKey() {
		return this.key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public boolean isEnable() {
		return this.enable;
	}
	
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
	public Socket getSocket() {
		return this.socket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public void addSocketListener(ISocketListener listener) {
		this.listener.add(listener);
	}
	
	public void removeSocketListener(ISocketListener listener) {
		this.listener.remove(listener);
	}
	
	public void run() {
		try {
			while(!socket.isClosed()) {
				if(enable) {
					is.read(head);
					if(FormatConverter.byte2int(head, 0) == 589505572) {
						int random = FormatConverter.byte2int(head, 4);
						int length = FormatConverter.byte2int(head, 13);
						body = new byte[length + 8];
						int off = 0;
						int len = 0;
						do {
							len = is.read(body, off, length+8-off);
							off += len;
						} while(off < length + 8);
						if(FormatConverter.byte2int(body, length) == random && FormatConverter.byte2int(body, length+4) == 606348067) {
							for(int i = 0; i < this.listener.size(); i++)
								this.listener.get(i).request(body, 0, length);
						}
					}
				}
			}
		} catch(IOException e) {
			log.error("RepSocket:", e);
			enable = false;
		}
	}
	
	public byte[] readData() {
		return null;
	}
	
	public int writeData(byte[] data) {
		return this.writeData(data, 0, data.length);
	}
	
	public int writeData(byte[] data, int offset, int length) {
		if(enable) {
			synchronized(os) {
				System.arraycopy(FormatConverter.int2byte(589505572), 0, rhead, 0, 4);
				int replyRandom = IDGenerater.generateRandomID();
				System.arraycopy(FormatConverter.int2byte(replyRandom), 0, rhead, 4, 4);
				System.arraycopy(FormatConverter.int2byte(0), 0, rhead, 8, 4);
				rhead[12] = 0x11;
				System.arraycopy(FormatConverter.int2byte(data.length), 0, rhead, 13, 4);
				
				rbody = new byte[length + 8];
				System.arraycopy(data, offset, rbody, 0, length);
				System.arraycopy(FormatConverter.int2byte(replyRandom), 0, rbody, length, 4);
				System.arraycopy(FormatConverter.int2byte(606348067), 0, rbody, length + 4, 4);
				
				try {
					os.write(rhead);
					os.write(rbody);
					os.flush();
					return rhead.length + rbody.length;
				} catch(IOException e) {
					log.error("RepSocket:", e);
					enable = false;
					return -1;
				}
			}
		} else {
			return -1;
		}
	}
	
	public void close() {
		this.enable = false;
		try {
			if(is != null)
				is.close();
			if(os != null)
				os.close();
			if(socket != null)
				socket.close();
		} catch (IOException e) {
			log.error("RepSocket:", e);
		}
	}
	
	public int available() {
		if(enable) {
			try {
				return this.is.available();
			} catch (IOException e) {
				enable = false;
				return 0;
			}
		} else {
			return 0;
		}
	}
}
