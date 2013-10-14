package com.socix.database;

public class MaxNumExceedException extends Exception {
	
	private static final long serialVersionUID = 6775482260777363009L;

	public MaxNumExceedException() {
    	super();
    }
    
    public MaxNumExceedException(String msg) {
    	super(msg);
    }
    
    public MaxNumExceedException(Throwable e) {
    	super(e);
    }
    
    public MaxNumExceedException(String msg, Throwable e) {
    	super(msg, e);
    }
    
}
