package com.socix.net.spi;

import java.net.Socket;
import java.net.SocketAddress;

public interface ISocket {
	public abstract boolean connect(SocketAddress endpoint);
	public abstract void close();
	public abstract String getKey();
	public abstract void setKey(String key);
	public abstract boolean isEnable();
	public abstract void setEnable(boolean enable);
	public abstract Socket getSocket();
	public abstract void setSocket(Socket socket);
	public abstract void addSocketListener(ISocketListener listener);
	public abstract void removeSocketListener(ISocketListener listener);
	public abstract byte[] readData();
	public abstract int writeData(byte[] data, int offset, int length);
	public abstract int available();
}