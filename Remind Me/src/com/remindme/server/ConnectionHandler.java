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
	private Socket socket;        //socket to client
	private SocketReader reader;  //read strings from client
	private SocketWriter writer;  //write strings to client
	
	public ConnectionHandler(Socket socket){
		this.socket = socket;
		this.reader = new SocketReader(socket);
		this.writer = new SocketWriter(socket);
	}
	
	/*
	 * Create new thread and handle the incoming connection.
	 */
	public void run(){
		RequestBuilder request_builder = new RequestBuilder();
		RequestManager request_manager = new RequestManager();
		RequestHandler request_handler = new RequestHandler();
		ResponseManager response_manager = new ResponseManager();
		UserManager user_manager = new UserManager();
		String in;
		
		/* CONTACT CLIENT, AND READ IN REQUEST */
		write("OK");
		in = read();
		
		/* CONVERT JSON STRING TO REQUEST OBJECT */
		Request request = request_builder.buildRequest(in);
		RequestResponse response;
		
		System.out.println(request);
		
		/* VERIFY CORE FIELDS */
		response = request_manager.verifyCoreFields(request);
		if(response != null){
			write(response.toString());
			disconnect();
			return;
		}

		/* VERIFY USERNAME/PASSWORD COMBO */
		User user = user_manager.authenticateUser(request.getUsername(), request.getPassword());
		if(user == null){
			response = response_manager.invalidCredientials();
			write(response.toString());
			disconnect();
			return;
		}
		request.setUser(user);
		
		/* VERIFY REMAINING REQUIRED FIELDS ARE PRESENT */
		response = request_manager.verifyRequestFields(request);
		if(response != null){
			write(response.toString());
			disconnect();
			return;
		}
		request.setContainsRequiredFields(true);
		
		/* VALIDATE REQUEST FIELDS */
		response = request_manager.confirmFields(request);
		if(response != null){
			write(response.toString());
			disconnect();
			return;
		}	
		request.setConfirmedFields(true);
	
		/* HANDLE THE REQUEST, AND SEND RESPONSE STRING */
		response = request_handler.handleRequest(request);
		write(response.toString());
		disconnect();
	}
	
	/*
	 * Close I/O and the socket
	 */
	private void disconnect(){
		try {
			this.reader.close();
			this.writer.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Write to the socket
	 */
	private void write(String s){
		this.writer.write(s);
	}
	
	/*
	 * read from the socket 
	 */
	private String read(){
		return this.reader.read();
	}
	
}
