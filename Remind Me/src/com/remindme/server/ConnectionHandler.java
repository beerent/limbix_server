package com.remindme.server;

import java.io.IOException;
import java.net.Socket;

import com.remindme.server.request.Request;
import com.remindme.server.request.RequestBuilder;
import com.remindme.server.request.RequestHandler;
import com.remindme.server.request.RequestManager;
import com.remindme.server.response.RequestResponse;
import com.remindme.server.response.ResponseManager;
import com.remindme.user.User;
import com.remindme.user.UserManager;

public class ConnectionHandler extends Thread{
	private Socket socket;
	private SocketReader reader;
	private SocketWriter writer;
	
	public ConnectionHandler(Socket socket){
		this.socket = socket;
		this.reader = new SocketReader(socket);
		this.writer = new SocketWriter(socket);
	}
	
	public void run(){
		RequestBuilder request_builder = new RequestBuilder();
		RequestManager request_manager = new RequestManager();
		RequestHandler request_handler = new RequestHandler();
		ResponseManager response_manager = new ResponseManager();
		UserManager user_manager = new UserManager();
		String in;
		
		write("OK");
		in = read();
		
		Request request = request_builder.buildRequest(in);
		RequestResponse response;
		
		/* VERIFY CORE FIELDS */
		response = request_manager.verifyCoreFields(request);
		if(response != null){
			write(response.toString());
			disconnect();
			return;
		}

		/* VERIFY USERNAME/PASSWORD COMBO */
		System.out.println("trying: " + request.getUsername() + " " + request.getPassword());
		User user = user_manager.authenticateUser(request.getUsername(), request.getPassword());
		if(user == null){
			System.out.println("invalid credentials");
			response = response_manager.invalidCredientials();
			write(response.toString());
			disconnect();
			return;
		}
		System.out.println(user.getUsername() + " successfully logged in");
		request.setUser(user);
		
		/* VERIFY REMAINING REQUIRED FIELDS ARE PRESENT */
		response = request_manager.vertifyRequestFields(request);
		if(response != null){
			write(response.toString());
			disconnect();
			return;
		}
		request.setContainsRequiredFields(true);
		System.out.println("request from " + user.getUsername() + " has all required fields");
		
		/* VALIDATE REQUEST FIELDS */
		response = request_manager.confirmFields(request);
		if(response != null){
			write(response.toString());
			System.out.println("request from " + user.getUsername() + " did not confirm fields");
			disconnect();
			return;
		}	
		request.setConfirmedFields(true);
		System.out.println("request from " + user.getUsername() + " confirmed fields");
	
		response = request_handler.handleRequest(request);
		write(response.toString());
		disconnect();
	}
	
	private void disconnect(){
		try {
			this.reader.close();
			this.writer.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("connection terminted");
	}
	
	private void write(String s){
		this.writer.write(s);
	}
	
	private String read(){
		return this.reader.read();
	}
	
}
