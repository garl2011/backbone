package com.socix.data.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.jdom.JDOMException;

import com.socix.data.spi.ICMDData;
import com.socix.util.FormatConverter;
import com.socix.xml.XMLMessage;

public class SimCmdImpl implements ICMDData {
	private String credential;
	private XMLMessage command;
	
	public SimCmdImpl() {
		credential = "";
	}

	public byte[] compose() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			if(this.command == null) {
				bos.write(FormatConverter.int2byte(0));
			} else {
				bos.write(FormatConverter.int2byte(this.command.out2Bytes().length));
				bos.write(this.command.out2Bytes());
			}
			
			bos.write(FormatConverter.int2byte(this.credential.length()));
			if(this.credential.length() > 0)
				bos.write(this.credential.getBytes());
			
			byte[] rv = bos.toByteArray();
			bos.close();
			return rv;
		} catch (IOException e) {
			throw e;
		}
	}

	public XMLMessage getCommand() {
		return command;
	}

	public Object getCredential() {
		return credential;
	}

	public void parse(byte[] data) throws IOException {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
		try {
			int xmlLen = dis.readInt();
			if(xmlLen > 0) {
				byte[] xmlByte = new byte[xmlLen];
				dis.read(xmlByte);
				this.command = XMLMessage.initXML(xmlByte);
			}
			
			int credLen = dis.readInt();
			byte[] credByte = new byte[credLen];
			dis.read(credByte);
			this.credential = new String(credByte);
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
			
			int xmlLen = dis.readInt();
			if(xmlLen > 0) {
				byte[] xmlByte = new byte[xmlLen];
				dis.read(xmlByte);
				this.command = XMLMessage.initXML(xmlByte);
			}
			
			int credLen = dis.readInt();
			byte[] credByte = new byte[credLen];
			dis.read(credByte);
			this.credential = new String(credByte);
		} catch (IOException e) {
			throw e;
		} catch (JDOMException e) {
			throw new IOException(e);
		}
	}

	public void setCommand(XMLMessage command) {
		this.command = command;
	}

	public void setCredential(Object credential) {
		this.credential = credential.toString();
	}

}
