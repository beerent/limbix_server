package com.remindme.server;

import java.io.IOException;
import java.net.Socket;

import com.remindme.server.request.Request;
import com.remindme.server.request.RequestBuilder;
import com.remindme.server.request.RequestHandler;
import com.remindme.server.request.RequestResponse;
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
		RequestHandler request_handler = new RequestHandler();
		UserManager user_manager = new UserManager();
		String in;
		
		write("OK");
		in = read();
		
		Request request = request_builder.buildRequest(in);
		
		System.out.println("trying: " + request.getUsername() + " " + request.getPassword());
		User user = user_manager.authenticateUser(request.getUsername(), request.getPassword());
		if(user == null){
			System.out.println("invalid credentials");
			//write invalid user
			disconnect();
			return;
		}
		
		System.out.println(user.getUsername() + " successfully logged in");
		
		request.setUser(user);
		
		boolean contains_required_fields = request_handler.containsRequiredFields(request);
		if(!contains_required_fields){
			System.out.println("request from " + user.getUsername() + " was missing required fields");
			//write missing fields
			disconnect();
			return;
		}
		
		request.setContainsRequiredFields(true);
		System.out.println("request from " + user.getUsername() + " has all required fields");
		
		
		boolean confirmed_fields = request_handler.confirmFields(request);
		if(!confirmed_fields){
			//write invalid fields
			System.out.println("request from " + user.getUsername() + " did not confirm fields");
			disconnect();
			return;
		}
		
		request.setConfirmedFields(true);
		System.out.println("request from " + user.getUsername() + " confirmed fields");
	
		RequestResponse response = request_handler.handleRequest(request);
	 
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
