package com.socix.logging;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class LogManager {
	private static String path = "config/log.xml";
	
	/**
	 * init log configure
	 *
	 */
	public static void init() {
		DOMConfigurator.configure(path);
	}
	
	/**
	 * init log configure with given file path
	 * @param filePath
	 */
	public static void init(String filePath) {
		path = filePath;
		init();
	}
	/**
	 * get logger with given name
	 * @param name
	 * @return 
	 */
	public static Logger getLogger(String name) {
		return Logger.getLogger(name);
	}
	
	/**
	 * get logger with given class
	 * @param cls
	 * @return
	 */
	public static Logger getLogger(Class cls) {
		return Logger.getLogger(cls);
	}
}