package com.socix.security.spi;

public interface IAuthentication {
	
	public static final int SUCCESS = 0;
	public static final int FAILURE = 1;
	
	public abstract int Authenticate(Object credential);
	
}