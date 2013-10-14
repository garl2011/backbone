package com.socix.data.spi;

import java.io.IOException;

import com.socix.xml.XMLMessage;

public interface IBSCData {
	public abstract XMLMessage getXML();
	public abstract void setXML(XMLMessage xml);
	public abstract byte[] getAttach(int index);
	public abstract byte[][] getAttachs();
	public abstract void addAttach(byte[] attach);
	public abstract void setAttachs(byte[][] attachs);
	public abstract byte[] compose() throws IOException;
	public abstract void parse(byte[] data, int offset, int length) throws IOException;
}