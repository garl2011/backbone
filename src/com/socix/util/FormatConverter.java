package com.socix.util;

public class FormatConverter {
	
	public static int byte2int(byte[] byteData) {
		return (((byteData[0] & 0xff) << 24) | ((byteData[1] & 0xff) << 16) | ((byteData[2] & 0xff) << 8) | (byteData[3] & 0xff));
	}
	
	public static int byte2int(byte[] byteData, int offset) {
		return (((byteData[offset] & 0xff) << 24) | ((byteData[offset+1] & 0xff) << 16) | ((byteData[offset+2] & 0xff) << 8) | (byteData[offset+3] & 0xff));
	}
	
	public static byte[] int2byte(int intData) {
		byte[] byteData = new byte[4];
		byteData[0] = (byte)(0xff & (intData >> 24));
		byteData[1] = (byte)(0xff & (intData >> 16));
		byteData[2] = (byte)(0xff & (intData >> 8));
		byteData[3] = (byte)(0xff & intData);
		return byteData;
	}
	
	public static short byte2short(byte[] byteData) {
		return (short)(((byteData[0] & 0xff) << 8) | (byteData[1] & 0xff));
	}
	
	public static short byte2short(byte[] byteData, int offset) {
		return (short)(((byteData[offset] & 0xff) << 8) | (byteData[offset+1] & 0xff));
	}
	
	public static byte[] short2byte(short shortData) {
		byte[] byteData = new byte[2];
		byteData[0] = (byte)(0xff & (shortData >> 8));
		byteData[1] = (byte)(0xff & shortData);
		return byteData;
	}
}
