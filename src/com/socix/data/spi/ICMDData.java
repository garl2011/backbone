package com.socix.data.spi;

import java.io.IOException;

import com.socix.xml.XMLMessage;

public interface ICMDData {
	public abstract XMLMessage getCommand();
	public abstract void setCommand(XMLMessage command);
	public abstract Object getCredential();
	public abstract void setCredential(Object credential);
	public abstract byte[] compose() throws IOException;
	public abstract void parse(byte[] data, int offset, int length) throws IOException;
}