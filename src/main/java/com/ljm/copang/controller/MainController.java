package com.ljm.copang.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ljm.copang.entity.Item;
import com.ljm.copang.entity.Member;
import com.ljm.copang.service.ItemService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	private final ItemService itemService;
	
	// 홈 화면
	@GetMapping("/")
	public String home(HttpServletRequest request, Model model) {
		// 물건의 정보를 전부 가져와서, model 객체에 넣어 전달
		List<Item> items = itemService.findItems();
		model.addAttribute("items", items);
		
		// 세션 확인
		HttpSession session = request.getSession(false);
		
		// 세션을 확인 했을 때, 로그인한 회원의 정보가 있다면, 회원의 정보를 모델객체에 담아준다.
		if(session != null) {
			// 만약 세션이 있다면, 로그인한 회원의 정보를 꺼내기
			Member loginMember = (Member) session.getAttribute("loginMember");
			if(loginMember != null) {
				model.addAttribute("member", loginMember);
			}
		}
		
		return "home";
	}
}