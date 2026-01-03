package com.ljm.copang.component;

import org.springframework.stereotype.Component;

import com.ljm.copang.entity.Member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
public class SessionManager {
	
	public Member getLoginMember(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session == null) {
			return null;
		}
		return (Member) session.getAttribute("loginMember");
	}
}
