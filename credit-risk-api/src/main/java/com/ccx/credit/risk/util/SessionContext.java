package com.ccx.credit.risk.util;

import javax.servlet.http.HttpServletRequest;
import com.ccx.credit.risk.model.User;

public class SessionContext {

	public static final String GLOBLE_USER_SESSION = "globle_user";
	
	public static void setUser(User user,HttpServletRequest request){
		if(user!=null){
			request.getSession().setAttribute(GLOBLE_USER_SESSION, user);
		}else{
			request.getSession().removeAttribute(GLOBLE_USER_SESSION);
		}
	}
	
	public static User get(HttpServletRequest request){
		return (User) request.getSession().getAttribute(GLOBLE_USER_SESSION);
	}
}
