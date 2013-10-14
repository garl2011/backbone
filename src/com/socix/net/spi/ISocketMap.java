package com.socix.net.spi;

public interface ISocketMap {
	public abstract boolean addRequester(String key, ISocket socket, Object credential);
	public abstract boolean addReplyer(String key, ISocket socket, Object credential);
	public abstract boolean addSubscriber(String key, ISocket socket, Object credential);
	public abstract boolean addPublisher(String key, ISocket socket, Object credential);
	public abstract ISocket getRequester(String key);
	public abstract ISocket getReplyer(String key);
	public abstract ISocket getSubscriber(String key);
	public abstract ISocket getPublisher(String key);
	public abstract boolean removeRequester(String key);
	public abstract boolean removeReplyer(String key);
	public abstract boolean removeSubscriber(String key);
	public abstract boolean removePublisher(String key);
}