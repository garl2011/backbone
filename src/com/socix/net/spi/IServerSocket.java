package com.socix.net.spi;

public interface IServerSocket {
	public abstract int initial(int port);
	public abstract void startConnection();
}