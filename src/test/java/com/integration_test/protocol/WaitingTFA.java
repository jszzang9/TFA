package com.integration_test.protocol;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.expull.tfa.TFALauncher;


public class WaitingTFA {

	public static void main(String[] list) throws Exception {
		System.out.println("");
		System.out.println("--------------------------------------");
		System.out.println(" Waiting TFA to be available");
		System.out.println("--------------------------------------");
		
		Thread.sleep(1000);
		
		boolean isExistMrsProcess = false;
		try {
			if (System.getProperty("os.name").startsWith("Windows")) {
				Process p = Runtime.getRuntime().exec(new String[] {"cmd", "/c", "tasklist /fi \"WINDOWTITLE eq tfa.jar\""});
				
				int lineCount = 0;
		        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		        while ((input.readLine()) != null)
		        	lineCount ++;
		        if (lineCount >= 2)
		        	isExistMrsProcess = true;
		        input.close();
			}
			else {
				Process p = Runtime.getRuntime().exec(new String[] {"sh", "-c", "ps -ef | grep tfa.jar | grep -v grep | awk '{print $2}'"});
				
				String line;
		        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		        while ((line = input.readLine()) != null) {
		        	if (line.length() > 0)
		        		isExistMrsProcess = true;
		        }
		        input.close();
			}
	    } catch (Exception err) {
	        err.printStackTrace();
	    }
		
		if (isExistMrsProcess == false) {
			System.out.println("First, MRS must be luanched.");
			System.out.println("--------------------------------------");
			System.out.println("");
			
			throw new Exception();
		}
		
		System.out.print("Waiting.");
		while(true) {
			try {
				Socket socket = new Socket();
				socket.connect(new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), TFALauncher.DEFAULT_APP_PORT));
				socket.close();
				
				System.out.println("\nOK.");
				break;
			} 
			catch (Exception e) {
				Thread.sleep(1000);
				System.out.print(".");
			}
		}
		
		System.out.println("--------------------------------------");
		System.out.println("");
	}
}
