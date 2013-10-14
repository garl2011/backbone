package com.socix.util;

import java.util.Random;

public class IDGenerater {
	
	private static Random randomGen = new Random();
	
	public static int generateRequestID() {
		return (int)(randomGen.nextDouble()*2000000000 + randomGen.nextDouble()*100000000);
	}
	
	public static int generateRandomID() {
		return (int)(randomGen.nextDouble()*Integer.MAX_VALUE);
	}
	
	public static int generateRequestSeq() {
		return (int)(System.currentTimeMillis()%Integer.MAX_VALUE);
	}
	
	public static int generateSocketKey() {
		return (int)(randomGen.nextDouble()*2000000000 + randomGen.nextDouble()*100000000);
	}
}
