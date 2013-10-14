package com.socix.data.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.socix.data.spi.ISPData;
import com.socix.util.FormatConverter;

public class SPDataImpl implements ISPData {
	private String topic;
	private byte[] content;
	
	public SPDataImpl() {
		topic = "";
	}

	public byte[] compose() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			bos.write(FormatConverter.int2byte(this.topic.length()));
			if(this.topic.length() > 0)
				bos.write(this.topic.getBytes());

			if(this.content == null) {
				bos.write(FormatConverter.int2byte(0));
			} else {
				bos.write(FormatConverter.int2byte(this.content.length));
				bos.write(this.content);
			}
			
			byte[] rv = bos.toByteArray();
			bos.close();
			return rv;
		} catch (IOException e) {
			throw e;
		}
	}

	public byte[] getContent() {
		return content;
	}

	public String getTopic() {
		return topic;
	}

	public void parse(byte[] data) throws IOException {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
		try {
			int topicLen = dis.readInt();
			if(topicLen > 0) {
				byte[] topicByte = new byte[topicLen];
				dis.read(topicByte);
				this.topic = new String(topicByte);
			}

			int contentLen = dis.readInt();
			if(contentLen > 0) {
				this.content = new byte[contentLen];
				dis.read(this.content);
			}
		} catch (IOException e) {
			throw e;
		}
	}
	
	public void parse(byte[] data, int offset, int length) throws IOException {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
		try {
			dis.skipBytes(offset);
			int topicLen = dis.readInt();
			if(topicLen > 0) {
				byte[] topicByte = new byte[topicLen];
				dis.read(topicByte);
				this.topic = new String(topicByte);
			}

			int contentLen = dis.readInt();
			if(contentLen > 0) {
				this.content = new byte[contentLen];
				dis.read(this.content);
			}
		} catch (IOException e) {
			throw e;
		}
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

}
