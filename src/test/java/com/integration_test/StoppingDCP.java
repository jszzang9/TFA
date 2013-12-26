package com.integration_test;

import java.io.File;

public class StoppingDCP {
	public static void main(String[] list) throws Exception {
		System.out.println("");
		System.out.println("--------------------------------------");
		System.out.println(" Stopping DCP for Integration Test");
		System.out.println("--------------------------------------");
		
		Thread.sleep(1000);
		
		if (System.getProperty("os.name").startsWith("Windows"))
			Runtime.getRuntime().exec(new File("src/test/resources/stop.bat").getAbsolutePath());
		else
			Runtime.getRuntime().exec(new File("src/test/resources/stop.sh").getAbsolutePath());
		
		System.out.println(" marlboro.dcp killed.");
		System.out.println("--------------------------------------");
		System.out.println("");
	}
}
