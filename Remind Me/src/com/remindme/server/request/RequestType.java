package com.remindme.server.request;

public enum RequestType {
	add,
	get,
	update_reminder,
	register_user ;

	/* RETURNS TRUE IF REQUEST TYPE A PRE LOGIN REQUEST */
	public static boolean isPreLoginRequest(RequestType request_type) {
		RequestType [] pre_login_requests = {register_user};
		for(RequestType type : pre_login_requests)
			if(type == request_type)
				return true;
		
		return false;
	}

	/* RETURNS TRUE IF REQUEST TYPE EXISTS AND REQUEST TYPE IS NOT A PRE LOGIN REQUEST */
	public static boolean isPostLoginRequest(RequestType request_type) {
		for(RequestType type : RequestType.values())
			if(type == request_type)
				return !isPreLoginRequest(request_type);
		return false;
	}
}
