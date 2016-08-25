package com.remindme.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RemindMeServer {
	public static void main(String[] args) {
		try {
			ServerSocket server_socket = new ServerSocket(PropertiesManager.getInstance().getPropertyInteger("server_port"));
			
			Socket socket;
			System.out.println("server started");
			while(true){
				socket = server_socket.accept();
				System.out.println("connection accepted");
				new ConnectionHandler(socket).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
