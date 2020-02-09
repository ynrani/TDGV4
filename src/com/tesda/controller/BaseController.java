package com.tesda.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;

import com.tesda.util.TdgCentralConstant;

@Controller
abstract class BaseController{
	private static Logger logger = Logger.getLogger(BaseController.class);
	private static String strClassName = " [ BaseController ] ";
	String checkSession(HttpServletRequest request, HttpServletResponse response){
		logger.info(strClassName + " ~~  inside of checkSession get method ");
		try {
			User user = (User) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			if (user == null || StringUtils.isEmpty(user.getUsername())) {
				return TdgCentralConstant.LOGIN_PAGE;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TdgCentralConstant.SESSION_CONTINUE;
	}
	
}
