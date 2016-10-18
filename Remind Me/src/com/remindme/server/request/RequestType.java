package com.remindme.server.request;

public enum RequestType {
	login,
	add,
	get,
	update_reminder,
	register_user, 
	update_user,
	add_filter, 
	get_filters_meta,
	get_filter,
	delete_filter,
	get_tags,
	update_gcm_token;

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
