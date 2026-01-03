package com.ljm.copang;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginCheckInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		String requestURI = request.getRequestURI();
		HttpSession session = request.getSession(false);
		
		if(session == null || session.getAttribute("loginMember") == null) {
			response.sendRedirect("/login?redirectURL=" + requestURI);
			return false;
		}
		
		return true;
	}
	
}
