package com.remindme.server;

import java.io.IOException;
import java.net.Socket;

import com.remindme.server.request.Request;
import com.remindme.server.request.RequestBuilder;
import com.remindme.server.request.RequestHandler;
import com.remindme.server.request.RequestManager;
import com.remindme.server.request.RequestType;
import com.remindme.server.response.RequestResponse;
import com.remindme.server.response.ResponseManager;
import com.remindme.user.User;
import com.remindme.user.UserManager;

public class ConnectionHandler extends Thread{
	private Socket socket;        //socket to client
	private SocketReader reader;  //read strings from client
	private SocketWriter writer;  //write strings to client

	private RequestBuilder  request_builder;
	private RequestManager  request_manager;
	private RequestHandler  request_handler;
	private ResponseManager response_manager;
	private UserManager     user_manager;
	
	public ConnectionHandler(Socket socket){
		this.socket = socket;
		this.reader = new SocketReader(socket);
		this.writer = new SocketWriter(socket);
		
		this.request_builder  = new RequestBuilder();
		this.request_manager  = new RequestManager();
		this.request_handler  = new RequestHandler();
		this.response_manager = new ResponseManager();
		this.user_manager     = new UserManager();
	}
	

	/***********************/
	/* SERVICE THE REQUEST */
	/***********************/
	@Override
	public void run(){
		String in;
		
		/* CONTACT CLIENT, AND READ IN REQUEST */
		write("OK");
		in = read();
		
		System.out.println("REQUEST: " + in);
		
		/* CONVERT JSON STRING TO REQUEST OBJECT */
		Request request = this.request_builder.buildRequest(in);
		RequestResponse response;
		
		/* VERIFY REQUEST TYPE */
		response = this.request_manager.verifyRequestType(request);
		if(response != null){
			write(response.toString());
			disconnect();
			return;
		}
		
		/* DETERMINE PRE OR POST LOGIN REQUEST */
		boolean is_pre  = RequestType.isPreLoginRequest(request.getRequestType());
		boolean is_post = RequestType.isPostLoginRequest(request.getRequestType());
		
		String response_str = null;
		if(is_pre)
			response_str = completePreLoginRequest(request);
		else if(is_post)
			response_str = completePostLoginRequest(request);
		
		/*
		 *  IF HERE, THE REQUEST TYPE DOES NOT EXISTS. 
		 *	REQUEST TYPES ARE EITHER PRE OR POST LOGIN REQUESTS
		 */
		else{
			response_str = response_manager.invalidRequestType().toString();
		}
		
		write(response_str);
		System.out.println("RESPONSE: " + response_str);
		
		disconnect();
	}
	
	
	
	/*********************/
	/* PRE LOGIN REQUEST */
	/*********************/	
	private String completePreLoginRequest(Request request) {
		return finishPreAndPostLoginRequest(request);
	}
	
	
	
	/**********************/
	/* POST LOGIN REQUEST */
	/**********************/
	private String completePostLoginRequest(Request request) {
		
		/* VERIFY USERNAME AND PASSWORD EXIST */
		RequestResponse response = this.request_manager.verifyRequestUsernameAndPasswordExist(request);
		if(response != null)
			return response.toString();
		System.out.println("A");
			
		/* VERIFY USERNAME AND PASSWORD COMBO */		
		User user = this.user_manager.authenticateUser(request.getUsername(), request.getPassword());
		if(user == null)
			return response_manager.invalidCredientials().toString();
		request.setUser(user); //user exists
		System.out.println("B");
		
		return finishPreAndPostLoginRequest(request);
	}
	
	
	
	/***********************/
	/* FINISH PRE AND POST */
	/***********************/
	private String finishPreAndPostLoginRequest(Request request){
		RequestResponse response;
		/* VERIFY REMAINING REQUIRED FIELDS ARE PRESENT */
		response = request_manager.verifyRequestFields(request);
		if(response != null)
			return response.toString();
		request.setContainsRequiredFields(true);
		
		System.out.println("C");	
		
		/* VALIDATE REQUEST FIELDS */
		response = request_manager.confirmFields(request);
		if(response != null)
			return response.toString();
		request.setConfirmedFields(true);
		
		System.out.println("D");
	
		/* HANDLE THE REQUEST, AND SEND RESPONSE STRING */
		response = request_handler.handleRequest(request);
		return response.toString();
	}

	
	
	/**********************/
	/*     DISCONNECT     */
	/**********************/
	private void disconnect(){
		try {
			this.reader.close();
			this.writer.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	
	/*********************/
	/*  WRITE TO SOCKET  */
	/*********************/
	private void write(String s){
		this.writer.write(s);
	}
	

	
	/**********************/
	/*  READ FROM SOCKET  */
	/**********************/
	private String read(){
		return this.reader.read();
	}	
}
