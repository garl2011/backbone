package com.socix.net.spi;

public interface ISocketListener {
	public abstract void command(byte[] data, int offset, int length);
	public abstract void request(byte[] data, int offset, int length);
	public abstract void reply(byte[] data, int offset, int length);
	public abstract void subscribe(byte[] data, int offset, int length);
	public abstract void publish(byte[] data, int offset, int length);
}