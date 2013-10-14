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

public class SampleSocket implements ISocket {
	
	private static Logger log = LogManager.getLogger(SampleSocket.class.getName());
	private Socket socket;
	private String key;
	private boolean enable = false;
	private List<ISocketListener> listener = new ArrayList<ISocketListener>();
	private InputStream is;
	private OutputStream os;
	private byte[] rhead = new byte[8];
	private byte[] whead = new byte[8];
	private byte[] rbody;
	private byte[] wbody;

	public SampleSocket() {
		this.socket = new Socket();
		this.key = String.valueOf(IDGenerater.generateSocketKey());
	}
	
	public SampleSocket(Socket socket) {
		this.socket = socket;
		this.key = String.valueOf(IDGenerater.generateSocketKey());
		try {
			this.is = this.socket.getInputStream();
			this.os = this.socket.getOutputStream();
			this.enable = true;
		} catch(IOException e) {
			this.enable = false;
			log.error("SampleSocket:", e);
		}
	}
	
	public void addSocketListener(ISocketListener listener) {
		this.listener.add(listener);
	}

	public int available() {
		if(enable) {
			try {
				return this.is.available();
			} catch (IOException e) {
				enable = false;
				log.error("SampleSocket:", e);
				return 0;
			}
		} else {
			return 0;
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
			log.error("SampleSocket:", e);
		}
	}

	public boolean connect(SocketAddress endpoint) {
		try {
			this.socket.connect(endpoint);
			this.is = this.socket.getInputStream();
			this.os = this.socket.getOutputStream();
			this.enable = true;
			return true;
		} catch (IOException e) {
			this.enable = false;
			log.error("SampleSocket:", e);
			return false;
		}
	}

	public String getKey() {
		return this.key;
	}

	public boolean isEnable() {
		return this.enable;
	}

	public byte[] readData() {
		byte[] data = null;
		if(enable) {
			try {
				is.read(rhead);
				if(FormatConverter.byte2int(rhead, 0) == 589505572) {
					int length = FormatConverter.byte2int(rhead, 4);
					rbody = new byte[length + 4];
					int off = 0;
					int len = 0;
					do {
						len = is.read(rbody, off, length+4-off);
						off += len;
					} while(off < length + 4);
					if(FormatConverter.byte2int(rbody, length) == 606348067) {
						data = new byte[length];
						System.arraycopy(rbody, 0, data, 0, length);
					}
				}
			} catch (IOException e) {
				enable = false;
				data = null;
				log.error("SampleSocket:", e);
			}
		}
		return data;
	}

	public void removeSocketListener(ISocketListener listener) {
		this.listener.remove(listener);
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public Socket getSocket() {
		return this.socket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public int writeData(byte[] data, int offset, int length) {
		int rs = -1;
		if(enable) {
			synchronized(os) {
				System.arraycopy(FormatConverter.int2byte(589505572), 0, whead, 0, 4);
				System.arraycopy(FormatConverter.int2byte(length), 0, whead, 4, 4);			
				rbody = new byte[length + 4];
				System.arraycopy(data, offset, wbody, 0, length);
				System.arraycopy(FormatConverter.int2byte(606348067), 0, wbody, length, 4);
				
				try {
					os.write(whead);
					os.write(wbody);
					os.flush();
					rs = whead.length + wbody.length;
				} catch(IOException e) {
					enable = false;
					rs = -1;
					log.error("SampleSocket:", e);
				}
			}
		}
		return rs;
	}

}
