package com.ljm.copang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ljm.copang.dto.LoginDto;
import com.ljm.copang.entity.Member;
import com.ljm.copang.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {
	
	private final MemberService memberService;
	
	@GetMapping("/login")
	public String loginForm(Model model) {
		model.addAttribute("loginForm", new LoginDto());
		return "login/loginForm";
	}
	
	@PostMapping("/login")
	public String login(@Valid @ModelAttribute("loginForm") LoginDto form, BindingResult result, HttpServletRequest request) {
		if(result.hasErrors()) {
			return "login/loginForm";
		}
		
		Member loginMember = memberService.login(form.getEmail(), form.getPassword());
		
		if(loginMember == null) {
			result.reject("loginError","아이디 또는 비밀번호가 맞지 않습니다.");
			return "login/loginForm";
		}
		
		// 로그인 성공 시
		HttpSession session = request.getSession();
		session.setAttribute("loginMember", loginMember);
		
		return "redirect:/";
	}
}
