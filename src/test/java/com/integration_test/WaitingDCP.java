package com.integration_test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.integration_test.protocol.ProtocolSendTestDataIT;
import com.marlboro.MarlboroBorn;


public class WaitingDCP extends ProtocolSendTestDataIT{
	
	public static void main(String[] list) throws Exception {
		System.out.println("");
		System.out.println("--------------------------------------");
		System.out.println(" Waiting DCP to be available");
		System.out.println("--------------------------------------");
		
		Thread.sleep(1000);
		
		boolean isExistDcpProcess = false;
		try {
			if (System.getProperty("os.name").startsWith("Windows")) {
				Process p = Runtime.getRuntime().exec(new String[] {"cmd", "/c", "tasklist /fi \"WINDOWTITLE eq marlboro.dcp.jar\""});
				
				int lineCount = 0;
		        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		        while ((input.readLine()) != null)
		        	lineCount ++;
		        if (lineCount >= 2)
		        	isExistDcpProcess = true;
		        input.close();
			}
			else {
				Process p = Runtime.getRuntime().exec(new String[] {"sh", "-c", "ps -ef | grep marlboro.dcp.jar | grep -v grep | awk '{print $2}'"});
				String line = null;
		        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		        while ((line = input.readLine()) != null) {
		        	if (line.length() > 0)
		        		isExistDcpProcess = true;
		        }
		        input.close();
			}
	    } catch (Exception err) {
	        err.printStackTrace();
	    }
		
		if (isExistDcpProcess == false) {
			System.out.println("First, DCP must be luanched.");
			System.out.println("--------------------------------------");
			System.out.println("");
			
			throw new Exception();
		}
		
		System.out.print("Waiting.");
		
		while(true) {
			try {
				Socket socket = new Socket();
				socket.connect(new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), MarlboroBorn.DEFAULT_PORT));
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
