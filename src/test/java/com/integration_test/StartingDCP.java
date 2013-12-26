package com.integration_test;

import java.io.File;

public class StartingDCP {
	public static void main(String[] list) throws Exception {
		System.out.println("");
		System.out.println("--------------------------------------");
		System.out.println(" Starting DCP for Integration Test");
		System.out.println("--------------------------------------");
		
		if (System.getProperty("os.name").startsWith("Windows")) {
			Runtime.getRuntime().exec(new File("src/test/resources/start.bat").getAbsolutePath());
		}
		else {
			Runtime.getRuntime().exec(new File("src/test/resources/start.sh").getAbsolutePath());
		}
			
		
		System.out.println(" marlboro.dcp started.");
		System.out.println("--------------------------------------");
		System.out.println("");
	}
}
