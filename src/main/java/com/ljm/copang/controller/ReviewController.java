package com.ljm.copang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ljm.copang.component.SessionManager;
import com.ljm.copang.dto.ReviewFormDto;
import com.ljm.copang.entity.Member;
import com.ljm.copang.entity.Order;
import com.ljm.copang.service.OrderService;
import com.ljm.copang.service.ReviewService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReviewController {
	
	private final OrderService orderService;
	private final SessionManager sessionManager;
	private final ReviewService reviewService;
	
	// 리뷰페이지 이동
	@GetMapping("/review/write")
	public String reviewForm(@RequestParam("orderId") Long orderId, Model model) {
		Order order = orderService.findOne(orderId);
		
		ReviewFormDto dto = new ReviewFormDto();
		dto.setOrderId(orderId);
		dto.setItemId(order.getOrderItems().get(0).getItem().getId());
		dto.setItemName(order.getOrderItems().get(0).getItem().getName());
		dto.setImageUrl(order.getOrderItems().get(0).getItem().getImageUrl());
		
		model.addAttribute("reviewDto", dto);
		
		return "review/review_form_view";
	}
	
	@PostMapping("/review/new")
	public String createReview(@Valid ReviewFormDto dto, BindingResult result, HttpServletRequest request) {
		if(result.hasErrors()) {
			return "review/review_form_view";
		}
		
		Member loginMember = sessionManager.getLoginMember(request);
		if(loginMember == null) {
			return "redirect:/login";
		}
		
		reviewService.saveReview(dto, loginMember.getId());
		return "redirect:/orders";
	}
}