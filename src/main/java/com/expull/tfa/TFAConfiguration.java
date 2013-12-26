package com.expull.tfa;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import com.expull.tfa.exception.MrsException;
import com.expull.tfa.exception.ServerInternalErrorException;

/**
 * Configuration 파일 정보를 로드하는 클래스이다.
 * 
 * @author delta829
 */
public class TFAConfiguration {
	private static Properties prop = new Properties();
	private static AtomicBoolean isLoad = new AtomicBoolean(false);
	
	public static void load(boolean isDeveloperMode) throws MrsException {
		if (isLoad.get()) 
			return;
		
		InputStream in = TFAConfiguration.class.getClassLoader().getResourceAsStream("config.properties");
		try {
			prop.load(in);
			in.close();
		}
		catch (Exception e) {
			throw new ServerInternalErrorException();
		}
		
		isLoad.set(true);
		
		if (isDeveloperMode) {
			setProperty("dcp.proxy.ip", getProperty("maven.dcp.proxy.ip"));
			setProperty("dcp.proxy.port", getProperty("maven.dcp.proxy.port"));
		}
		
		printProps();
	}
	

	public static String getProperty(String key) throws MrsException {
		if (isLoad.get() == false)
			load(false);
		
		return prop.getProperty(key, "");
	}
	
	public static void setProperty(String key, String value) {
		prop.setProperty(key, value);
	}
	
	public static void printProps() {
		Iterator<Object> keyNum = prop.keySet().iterator();
		System.out.println("* config properties");
		while ( keyNum.hasNext() ) {
			String key = (String)keyNum.next();
			System.out.println("*** " + key + ":" + prop.getProperty(key));
		}
	}
}
