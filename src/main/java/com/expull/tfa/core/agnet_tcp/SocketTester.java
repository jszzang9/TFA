package com.expull.tfa.core.agnet_tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketTester {
	static class Client extends Thread {
		private final Socket socket;

		public Client(Socket socket) { this.socket = socket; }
		@Override
		public void run() {
			byte buf[] = {0};
			while(true) {
				java.io.OutputStream out;
				try {
					out = socket.getOutputStream();
					out.write(buf);
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
		}
	}
	
	static class Server {
		private final int port;
		ServerSocket server;
		public Server(int port) {
			this.port = port;
		}

		public void start() {
			try {
				server = new ServerSocket(port);
				while(true) {
					Socket socket = server.accept();
					Client client = new Client(socket);
					client.start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	public static void main(String...args) {
		(new Server(10401)).start();
	}
}
