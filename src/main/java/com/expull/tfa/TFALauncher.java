package com.expull.tfa;

import java.util.HashMap;

import com.expull.tfa.core.agent_was.AgentWasServer;
import com.expull.tfa.core.agent_websocket.AgentWebSocketServer;
import com.expull.tfa.core.agnet_tcp.AgentTcpServer;
import com.expull.tfa.core.app.AppServer;

/**
 * MRS 런처 클래스이다.
 * 
 * @author delta829
 */
public class TFALauncher {
	public static final int DEFAULT_APP_PORT = 10501;
	public static final int DEFAULT_AGENT_TCP_PORT = 10401;
	public static final int DEFAULT_AGENT_WAS_PORT = 10481;
	public static final int DEFAULT_AGENT_WEBSOCKET_PORT = 10405;

	/**
	 * 런처 메인.
	 * 
	 * @param args 파라미터들.
	 */
	public static void main(String args[]) {
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
		
		int app_port = DEFAULT_APP_PORT;
		int agent_tcp_port = DEFAULT_AGENT_TCP_PORT;
		int agent_was_port = DEFAULT_AGENT_WAS_PORT;
		int agent_websocket_port = DEFAULT_AGENT_WEBSOCKET_PORT;
		try {
			if (options.containsKey("h"))
				showHelp = true;
			if (options.containsKey("a"))
				app_port = Integer.parseInt(options.get("a"));
			if (options.containsKey("t"))
				agent_tcp_port = Integer.parseInt(options.get("t"));
			if (options.containsKey("w"))
				agent_was_port = Integer.parseInt(options.get("w"));
			if (options.containsKey("i"))
				agent_websocket_port = Integer.parseInt(options.get("i"));
		} catch (Exception e) {
			showHelp = true;
		}
		
		if (showHelp) {
			printHelp();
			return;
		}
		
		
		AppServer appServer = new AppServer(app_port);
		appServer.run();
		
		AgentTcpServer agentTcpServer = new AgentTcpServer(agent_tcp_port);
		agentTcpServer.run();
		
		AgentWebSocketServer agentSoketServer = new AgentWebSocketServer(agent_websocket_port);
		agentSoketServer.run();
		
		AgentWasServer agentWasServer = new AgentWasServer(agent_was_port);
		agentWasServer.run();
	}
	
	/**
	 * 도움말을 출력한다.
	 */
	private static void printHelp() {
		System.out.println("Magic Relay Server Usage.\n");
		System.out.println(" rowem.mrs [port] [options]\n");
		System.out.println("[port]");
		System.out.println("  -a[n]\t\tApp Server Port Number. Default is " + DEFAULT_APP_PORT);
		System.out.println("  -t[n]\t\tAgent TCP Server Port Number. Default is " + DEFAULT_AGENT_TCP_PORT);
		System.out.println("  -i[n]\t\tAgent WebSocket Server Port Number. Default is " + DEFAULT_AGENT_WEBSOCKET_PORT);
		System.out.println("  -w[n]\t\tAgent WAS Server Port Number. Default is " + DEFAULT_AGENT_WAS_PORT + "\n");
		System.out.println("[options]");
		System.out.println("  -h   \t\tPrint help.\n");
		System.out.println("[examples]");
		System.out.println("  rowem.mrs");
		System.out.println("  rowem.mrs -a12345 -w12121");
		System.out.println("  rowem.mrs -h");
	}
}
