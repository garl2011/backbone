package com.socix.service.spi;

import com.socix.data.spi.IRRData;

public interface IService {
	
	public static final int SUCCESS = 0;
	public static final int FAILURE = 1;
	
	public abstract int init();
	public abstract int runService(IRRData request, IRRData reply);
	public abstract int fin();
	public abstract IService copy();
	
}