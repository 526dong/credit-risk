package com.ccx.credit.risk.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;

import com.ccx.credit.risk.model.User;

/**
 * 获取用户信息和请求参数
 * @author xzd
 * @date 2017/6/12
 * @version
 */
public class ControllerUtil {

	/**
	 * 获取请求参数
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static  Map<String,Object> requestMap(HttpServletRequest request){
		Enumeration emu = request.getParameterNames();
		Map<String,Object> requestMap = new HashMap<String,Object>();
		while(emu.hasMoreElements()){
			String name = (String)emu.nextElement();
			String value = request.getParameter(name);
			
			boolean flag = (value == null || "".equals(value.toString().trim()));
			
			if(!flag){
				requestMap.put(name, value.trim());
			}			
		}		
		return requestMap;
	}
	
	/**
	 * 获取登陆的用户信息
	 * @param req HttpServletRequest
	 * @return user 
	 */ 
	public static User getSessionUser(HttpServletRequest req){
		HttpSession session = req.getSession();
		User user = (User)session.getAttribute("risk_crm_user");
//		User user = (User)SecurityUtils.getSubject().getPrincipal();
    	return user;
	}
	

	
}