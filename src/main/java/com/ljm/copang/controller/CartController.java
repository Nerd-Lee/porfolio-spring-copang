package com.ljm.copang.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ljm.copang.component.SessionManager;
import com.ljm.copang.entity.Cart;
import com.ljm.copang.entity.CartItem;
import com.ljm.copang.entity.Member;
import com.ljm.copang.repository.CartItemRepository;
import com.ljm.copang.repository.CartRepository;
import com.ljm.copang.service.CartService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CartController {
	private final CartService cartService;
	private final SessionManager sessionManager;
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	
	@GetMapping("/cart")
	public String cartList(HttpServletRequest request, Model model) {
		Member loginMember = sessionManager.getLoginMember(request);
		
		if(loginMember == null) {
			return "redirect:/login";
		}
		
		Cart cart = cartRepository.findByMemberId(loginMember.getId());
		List<CartItem> cartItems = new ArrayList<>();
		
		if(cart != null) {
			cartItems = cartItemRepository.findByCartId(cart.getId());
		}
		
		model.addAttribute("cartItems", cartItems);
		
		int totalPrice = cartItems.stream()
						.mapToInt(ci -> ci.getItem().getPrice() * ci.getCount())
						.sum();
		
		model.addAttribute("totalPrice", totalPrice);
		
		return "cart/cart_list_view";
	}
	
	// 카트에 상품 추가
	@PostMapping("/cart/add")
	public String addCart(@RequestParam Long itemId, @RequestParam int count, HttpServletRequest request) {
		Member loginMember = sessionManager.getLoginMember(request);
		
		if(loginMember == null) {
			return "redirect:/login";
		}
		
		cartService.addCart(loginMember, itemId, count);
		return "redirect:/cart";
	}
	
	// 장바구니에 해당 상품 삭제
	@PostMapping("/cart/item/{cartItemId}/delete")
	public String deleteCartItem(@PathVariable Long cartItemId) {
		cartService.deleteCartItem(cartItemId);
		return "redirect:/cart";
	}
	
	// 장바구니 수량 수정 기능 ( 기능 다 만든 후, AJAX 비동기 방식으로 바꾸자. )
	@PostMapping("/cart/item/{cartItemId}/update")
	public String updateCartItem(@PathVariable Long cartItemId, @RequestParam int count) {
		cartService.updateCartItemCount(cartItemId, count);
		return "redirect:/cart";
	}
	
	// 장바구니 주문
	@PostMapping("/cart/order")
	public String orderCart(HttpServletRequest request) {
		Member loginMember = sessionManager.getLoginMember(request);
		if(loginMember == null) {
			return "redirect:/login";
		}
		
		cartService.orderCartItems(loginMember.getId());
		
		return "redirect:/";
	}
}
