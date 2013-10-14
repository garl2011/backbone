package com.socix.data.spi;

import java.io.IOException;

public interface ISPData {
	public abstract String getTopic();
	public abstract void setTopic(String topic);
	public abstract byte[] getContent();
	public abstract void setContent(byte[] content);
	public abstract byte[] compose() throws IOException;
	public abstract void parse(byte[] data, int offset, int length) throws IOException;
}