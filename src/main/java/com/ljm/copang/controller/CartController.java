package com.ljm.copang.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ljm.copang.component.SessionManager;
import com.ljm.copang.dto.OrderFormDto;
import com.ljm.copang.entity.Cart;
import com.ljm.copang.entity.CartItem;
import com.ljm.copang.entity.Member;
import com.ljm.copang.exception.NotEnoughStockException;
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
	
	@PostMapping("/cart/order/form")
	public String cartOrderForm(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		Member loginMember = sessionManager.getLoginMember(request);
		if(loginMember == null) {
			return "redirect:/login";
		}
		
		Cart cart = cartRepository.findByMemberId(loginMember.getId());
		List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
		
		if(cartItems.isEmpty()) {
			return "redirect:/cart";
		}
		
		// 만약 주문 전 재고가 없다면?
		
		List<String> outOfStockItems = new ArrayList<>();
		List<CartItem> validItems = new ArrayList<>();
		
		// 내가 주문한 아이템의 수량과 아이템의 현재 수량이 여유가 있는 지 확인
		for(CartItem ci : cartItems) {
			if(ci.getItem().getStockQuantity() < ci.getCount()) {
				outOfStockItems.add(ci.getItem().getName());
			}
			else {
				validItems.add(ci);
			}
		}
		
		// 만약 재고가 부족한 아이템이 있다면?
		if(!outOfStockItems.isEmpty()) {
			String msg = String.join(", ", outOfStockItems) + " 상품의 재고가 부족하여 제외되었습니다.";
			redirectAttributes.addFlashAttribute("errorMessage", msg);
			return "redirect:/cart";
		}
		
		// 재고가 부족한 아이템이 없다면, 장바구니에서 주문할 아이템의 정보를 전부 주문페이지로 보낸다.
		OrderFormDto form = new OrderFormDto();
		form.setReceiverName(loginMember.getName());
		
		int total = 0;
		for(CartItem ci : cartItems) {
			OrderFormDto.OrderItemDto dto = new OrderFormDto.OrderItemDto(
					ci.getItem().getId(),
					ci.getItem().getName(),
					ci.getItem().getPrice(),
					ci.getCount(),
					ci.getItem().getImageUrl()
			);
			form.getOrderItems().add(dto);
			total += ci.getItem().getPrice() * ci.getCount();
		}
		form.setTotalPrice(total);
		
		model.addAttribute("orderForm", form);
		return "order/order_form_view";
	}
}
