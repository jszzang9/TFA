package com.integration_test.protocol;

import java.io.File;

public class StartingTFA {

	public static void main(String[] list) throws Exception {
		System.out.println("");
		System.out.println("--------------------------------------");
		System.out.println(" Starting TFA for Integration Test");
		System.out.println("--------------------------------------");
		
		if (System.getProperty("os.name").startsWith("Windows"))
			Runtime.getRuntime().exec(new File("src/test/resources/start.bat").getAbsolutePath());
		else
			Runtime.getRuntime().exec(new File("src/test/resources/start.sh").getAbsolutePath());
		
		System.out.println(" com.expull.tfa started.");
		System.out.println("--------------------------------------");
		System.out.println("");
	}
}
