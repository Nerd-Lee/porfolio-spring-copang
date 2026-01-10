package com.ljm.copang.component;

import org.springframework.stereotype.Component;

import com.ljm.copang.entity.Member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Component
public class SessionManager {
	
	// 로그인했을 때 세션이 있는 지 없는지를 확인하는 함수
	public Member getLoginMember(HttpServletRequest request) {
		/* 
		 * 세션이 있으면 기존 세션을 반환하고, 세션이 없다면 null을 반환
		 * 단순하게 현재 로그인을 한 상태인 지 확인하기 위해서
		 */
		HttpSession session = request.getSession(false);
		
		// 세션이 null이라면, 로그인 하지 않은 상태니깐 null을 반환
		if(session == null) {
			return null;
		}
		
		// 로그인을 한다면, Member의 정보를 SetAttribute에 저장이 되어 있어서, 멤버 객체를 반환한다.
		return (Member) session.getAttribute("loginMember");
	}
}