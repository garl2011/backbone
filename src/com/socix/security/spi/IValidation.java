package com.socix.security.spi;

public interface IValidation {

	public static final int SUCCESS = 0;
	public static final int FAILURE = 1;
	
	public abstract int validate(Object ticket);
	
}