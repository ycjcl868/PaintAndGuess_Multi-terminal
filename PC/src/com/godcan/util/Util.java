package com.godcan.util;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * 加载传输端口和IP
 * @author Spirit
 *
 */
public class Util {
	public static String IP; 
	public static int port;
	
	static {
		Properties p = new Properties();
		try {
			p.load(new FileInputStream("config.properties"));
			IP = p.getProperty("IP");
			port = Integer.parseInt(p.getProperty("port"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
