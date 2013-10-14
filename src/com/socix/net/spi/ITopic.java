package com.socix.net.spi;

public interface ITopic {
	public abstract String getTopic();
	public abstract void setTopic(String topic);
	public abstract boolean addSubscriber(Object subscriber);
	public abstract boolean removeSubscriber(Object subscriber);
	public abstract boolean addSubTopic(ITopic subTopic);
	public abstract boolean removeSubTopic(String key);
	public abstract void publish(byte[] data, int offset, int length);
}