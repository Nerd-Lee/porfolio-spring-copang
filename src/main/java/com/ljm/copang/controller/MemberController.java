package com.ljm.copang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ljm.copang.component.SessionManager;
import com.ljm.copang.dto.MemberJoinDto;
import com.ljm.copang.dto.PasswordUpdateDto;
import com.ljm.copang.entity.Item;
import com.ljm.copang.entity.Member;
import com.ljm.copang.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
	
	private final MemberService memberService;
	private final SessionManager sessionManager;
	
	@GetMapping("/new")
	public String createForm(Model model) {
		model.addAttribute("memberForm", new MemberJoinDto());
		return "members/createMember";
	}
	
	// 패스워드 페이지 이동
	@GetMapping("/password/edit")
	public String passwordEditForm(HttpServletRequest request, Model model) {
		Member loginMember = sessionManager.getLoginMember(request);
		if(loginMember == null) {
			return "redirect:/login";
		}
		
		model.addAttribute("passwordUpdateDto", new PasswordUpdateDto());
		return "members/password_edit_form";
	}
	
	@PostMapping("/new")
	public String createMember(@Valid @ModelAttribute("memberForm") MemberJoinDto form, BindingResult result) {
		if(result.hasErrors()) {
			return "members/createMember";
		}
		
		memberService.join(form);
		return "redirect:/";
	}
	
	@PostMapping("/password/edit")
	public String passwordEdit(@Valid @ModelAttribute("passwordUpdateDto") PasswordUpdateDto dto, HttpServletRequest request, BindingResult result) {
		Member loginMember = sessionManager.getLoginMember(request);
		if(loginMember == null) {
			return "redirect:/login";
		}
		
		// 입력 데이터 검증하기
		if(result.hasErrors()) {
			return "members/password_edit_form";
		}
		
		try {
			memberService.updatePassword(loginMember.getId(), dto);
		}
		catch(IllegalStateException e) {
			result.reject("passwordError", e.getMessage());
			return "members/password_edit_form";
		}
		
		return "redirect:/";
	}
}