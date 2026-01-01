package com.ljm.copang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ljm.copang.dto.MemberJoinDto;
import com.ljm.copang.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
	
	private final MemberService memberService;
	
	@GetMapping("/new")
	public String createForm(Model model) {
		model.addAttribute("memberForm", new MemberJoinDto());
		return "members/createMember";
	}
	
	@PostMapping("/new")
	public String createMember(@Valid @ModelAttribute("memberForm") MemberJoinDto form, BindingResult result) {
		if(result.hasErrors()) {
			return "members/createMember";
		}
		
		memberService.join(form);
		return "redirect:/";
	}
}
