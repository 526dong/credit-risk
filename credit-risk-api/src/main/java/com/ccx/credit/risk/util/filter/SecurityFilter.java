package com.ccx.credit.risk.util.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.ccx.credit.risk.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * 过滤所有请求
 * 
 * 判断当前用户是否有效经过一下步骤.
 * 
 */
public class SecurityFilter implements Filter {
	private String whiteList = "";
	private String loginPage = "/login";
//	private String regularWhiteList = "";
    private static Logger logger = LogManager.getLogger(SecurityFilter.class);

	public SecurityFilter() {
	}

	public void init(FilterConfig fConfig) throws ServletException {
		this.whiteList = fConfig.getInitParameter("whiteList");
		this.loginPage = fConfig.getInitParameter("loginPage");
//		this.regularWhiteList = fConfig.getInitParameter("regularWhiteList");
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;


		req.setCharacterEncoding("utf-8");
		res.setCharacterEncoding("utf-8");
		String path = req.getContextPath();
		String basePath = req.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		String servlet = req.getServletPath();
		
		if (null != servlet && !servlet.equals("")) {
			servlet = servlet.substring(1);
		}
		boolean doFilter = true;
		if (servlet.contains("resources") || servlet.contains("css") || servlet.contains("js")
				|| servlet.contains("static")|| servlet.contains("commons")) {
			doFilter = false;
		}

		// 需要校验
		if (doFilter && (this.whiteList.indexOf(servlet) == -1 )) {
			try {
				HttpSession session = req.getSession();
				// logger.info("测试session:");
				// logger.info("filter:"+session.getId());
				// Rights rights = (Rights) session.getAttribute("rights");
				User zxuser = (User) session.getAttribute("risk_crm_user");
				if (zxuser == null) {
					logger.debug("filter:risk_crm_user:" + zxuser);
					String gopage = loginPage;
					logger.debug("重定向URL:" + basePath + gopage);
					res.sendRedirect(basePath + gopage);
					return;
				}
			} catch (Exception e) {
				res.sendRedirect(basePath);
				return;
			}
		} else if (doFilter && servlet.contains("login") && req.getMethod().toLowerCase().equals("get")) {
			String gopage = loginPage;
			logger.debug("重定向URL:" + basePath + gopage);
			res.sendRedirect(basePath + gopage);
			return;
		}
		
		chain.doFilter(request, response);
	}

}
