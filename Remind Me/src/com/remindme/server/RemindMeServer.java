package com.remindme.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RemindMeServer {
	public static void main(String[] args) {
		try {
			ServerSocket server_socket = new ServerSocket(1313);
			
			Socket socket;
			while(true){
				socket = server_socket.accept();
				new ConnectionHandler(socket).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
