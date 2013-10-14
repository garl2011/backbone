package com.socix.data.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.JDOMException;

import com.socix.data.spi.IRRData;
import com.socix.util.FormatConverter;
import com.socix.xml.XMLMessage;

public class RRDataImpl implements IRRData {
	private XMLMessage  msg;
	private List<byte[]> attachs;
	
	public String path;
	
	public RRDataImpl() {
		path = "";
		attachs = new ArrayList<byte[]>();
	}
	
	public XMLMessage getMSG() {
		return msg;
	}
	public void setMSG(XMLMessage req) {
		this.msg = req;
	}
	
	public byte[] getAttach(int index) {
		if(index < attachs.size())
			return attachs.get(index);
		else 
			return null;
	}
	
	public byte[][] getAttachs() {
		if(attachs.size() > 0)
			return attachs.toArray(new byte[0][]);
		else
			return null;
	}
	public void addAttach(byte[] attach) {
		attachs.add(attach);
	}
	public void setAttachs(byte[][] attachs) {
		this.attachs.clear();
		if(attachs != null)
			for(int i = 0; i< attachs.length; i++)
				this.attachs.add(attachs[i]);
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	public byte[] compose() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			bos.write(FormatConverter.int2byte(this.path.length()));
			if(this.path.length() > 0)
				bos.write(this.path.getBytes());
			
			if(this.msg == null) {
				bos.write(FormatConverter.int2byte(0));
			} else {
				bos.write(FormatConverter.int2byte(this.msg.out2Bytes().length));
				bos.write(this.msg.out2Bytes());
			}
			
			if(this.attachs.size() == 0) {
				bos.write(0);
			} else {
				bos.write(this.attachs.size());
				for(int i = 0; i < this.attachs.size(); i++)
					bos.write(FormatConverter.int2byte(this.attachs.get(i).length));
			}
			if(this.attachs.size() > 0)
				for(int i = 0; i < this.attachs.size(); i++)
					bos.write(this.attachs.get(i));
			
			byte[] rv = bos.toByteArray();
			bos.close();
			return rv;
		} catch (IOException e) {
			throw e;
		}
	}
	public void parse(byte[] data) throws IOException {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
		try {
			int pathLen = dis.readInt();
			byte[] pathByte = new byte[pathLen];
			dis.read(pathByte);
			this.path = new String(pathByte);
			
			int xmlLen = dis.readInt();
			if(xmlLen > 0) {
				byte[] xmlByte = new byte[xmlLen];
				dis.read(xmlByte);
				this.msg = XMLMessage.initXML(xmlByte);
			}
			
			int attCount = dis.readByte();
			if(attCount > 0) {
				byte[][] att = new byte[attCount][];
				for(int i = 0; i < attCount; i++)
					att[i] = new byte[dis.readInt()];

				for(int i = 0; i < attCount; i++)
					dis.read(att[i]);
				
				setAttachs(att);
			}
		} catch (IOException e) {
			throw e;
		} catch (JDOMException e) {
			throw new IOException(e);
		}
	}
	
	public void parse(byte[] data, int offset, int length) throws IOException {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
		try {
			dis.skipBytes(offset);
			int pathLen = dis.readInt();
			byte[] pathByte = new byte[pathLen];
			dis.read(pathByte);
			this.path = new String(pathByte);
			
			int xmlLen = dis.readInt();
			if(xmlLen > 0) {
				byte[] xmlByte = new byte[xmlLen];
				dis.read(xmlByte);
				this.msg = XMLMessage.initXML(xmlByte);
			}
			
			int attCount = dis.readByte();
			if(attCount > 0) {
				byte[][] att = new byte[attCount][];
				for(int i = 0; i < attCount; i++)
					att[i] = new byte[dis.readInt()];

				for(int i = 0; i < attCount; i++)
					dis.read(att[i]);
				
				setAttachs(att);
			}
		} catch (IOException e) {
			throw e;
		} catch (JDOMException e) {
			throw new IOException(e);
		}
	}
}
