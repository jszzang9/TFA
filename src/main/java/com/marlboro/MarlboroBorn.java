package com.marlboro;

import java.util.HashMap;

import org.apache.log4j.Level;

import com.marlboro.core.server.Server;
import com.marlboro.util.QueuedLogger.QueuedLogger;


public class MarlboroBorn {
public static final int DEFAULT_PORT = 3231;
	
	public static void main(String...args) throws Exception {
		boolean showHelp = false;
		HashMap<String, String> options = new HashMap<String, String>();
		
		for (int i = 0; i < args.length; i ++) {
			String arg = args[i];
			if (arg.length() <= 1) {
				showHelp = true;
				break;
			}
			if (arg.substring(0, 1).equals("-") == false) {
				showHelp = true;
				break;
			}
			
			String key = arg.substring(1, 2);
			String value = (arg.length() > 2 ? arg.substring(2) : "");
			options.put(key, value);
		}
		
		int port = DEFAULT_PORT;
		
		try {
			if (options.containsKey("h"))
				showHelp = true;
			if (options.containsKey("p"))
				port = Integer.parseInt(options.get("p"));
		} catch (Exception e) {
			showHelp = true;
		}
		
		if (showHelp) {
			printHelp();
			return;
		}
		QueuedLogger.push(Level.INFO, "DCP starting....");
		
		Server server = new Server();
		server.run(port);
	}

	/**
	 * 도움말을 출력한다.
	 */
	private static void printHelp() {
		System.out.println("Data Control Processor Usage.\n");
		System.out.println(" rowem.dcp [port] [options]\n");
		System.out.println("[port]");
		System.out.println("  -p[n]\t\tPort Number. Default is " + DEFAULT_PORT + "\n");
		System.out.println("[options]");
		System.out.println("  -h   \t\tPrint help.\n");
		System.out.println("[examples]");
		System.out.println("  rowem.dcp");
		System.out.println("  rowem.dcp -p12345");
		System.out.println("  rowem.dcp -h");
	}
}
