package com.ljm.copang.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ljm.copang.component.SessionManager;
import com.ljm.copang.entity.Member;
import com.ljm.copang.entity.Order;
import com.ljm.copang.repository.OrderRepository;
import com.ljm.copang.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OrderController {
	
	private final OrderService orderService;
	private final SessionManager sessionManager;
	private final OrderRepository orderRepository;
	
	@GetMapping("/orders")
	public String orderListView(HttpServletRequest request, Model model) {
		Member loginMember = sessionManager.getLoginMember(request);
		if(loginMember == null) {
			return "redirect:/login";
		}
		
		List<Order> orders = orderRepository.findByMemberId(loginMember.getId());
		
		model.addAttribute("orders", orders);
		
		return "order/order_list_view";
	}
	
	@PostMapping("/order/{itemId}")
	public String order(@PathVariable Long itemId, @RequestParam int count, HttpServletRequest request) {
		Member loginMember = sessionManager.getLoginMember(request);
		
		if(loginMember == null) {
			return "redirect:/login";
		}
		
		orderService.order(loginMember.getId(), itemId, count);
		
		return "redirect:/";
	}
	
	@PostMapping("/orders/{orderId}/cancel")
	public String cancelOrder(@PathVariable Long orderId) {
		orderService.cancelOrder(orderId);
		return "redirect:/orders";
	}
}
