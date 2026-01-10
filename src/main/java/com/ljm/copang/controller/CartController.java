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

// 장바구니 컨트롤러
@Controller
@RequiredArgsConstructor
public class CartController {
	private final CartService cartService;
	private final SessionManager sessionManager;
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	
	// 장바구니 페이지 이동
	@GetMapping("/cart")
	public String cartList(HttpServletRequest request, Model model) {
		/*
		 * 로그인한 상태인 지 확인
		 * 로그인 한 상태가 아니라면, 로그인 페이지로 redirect
		 */
		Member loginMember = sessionManager.getLoginMember(request);
		if(loginMember == null) {
			return "redirect:/login";
		}
		
		/* 
		 * 로그인을 했다면, 로그인한 멤버의 장바구니의 정보를 Cart 객체에 대입
		 * 장바구니에 저장되어 있는 아이템을 가져오기 위한 리스트를 생성
		 */
		Cart cart = cartRepository.findByMemberId(loginMember.getId());
		List<CartItem> cartItems = new ArrayList<>();
		
		/*
		 * 장바구니가 없다면, 아이템이 장바구니에 추가가 되어 있다는 말이기 때문에
		 * 장바구니에 저장되어 있는 아이템을 리스트에 저장
		 */
		if(cart != null) {
			cartItems = cartItemRepository.findByCartId(cart.getId());
		}
		
		// 장바구니 아이템 리스트를 cart_list_view라는 html에 보낸다.
		model.addAttribute("cartItems", cartItems);
		
		/*int totalPrice = cartItems.stream()
						.mapToInt(ci -> ci.getItem().getPrice() * ci.getCount())
						.sum();
		
		model.addAttribute("totalPrice", totalPrice);
		*/
		
		return "cart/cart_list_view";
	}
	
	// 카트에 상품 추가
	@PostMapping("/cart/add")
	public String addCart(@RequestParam Long itemId, @RequestParam int count, HttpServletRequest request) {
		/*
		 * 로그인한 상태인 지 확인
		 * 로그인 한 상태가 아니라면, 로그인 페이지로 redirect
		 */
		Member loginMember = sessionManager.getLoginMember(request);
		if(loginMember == null) {
			return "redirect:/login";
		}
		
		// CartService 클래스에 있는 addCart라는 함수를 실행 후, 장바구니 페이지로 redirect 
		cartService.addCart(loginMember, itemId, count);
		return "redirect:/cart";
	}
	
	// 장바구니에 개별 상품 삭제
	@PostMapping("/cart/item/{cartItemId}/delete")
	public String deleteCartItem(@PathVariable Long cartItemId) {
		// CartService 클래스에 있는 deleteCartItem이라는 함수를 실행 후 장바구니 페이지로 redirect
		cartService.deleteCartItem(cartItemId);
		return "redirect:/cart";
	}
	
	// 장바구니에서 선택 상품 삭제
	@PostMapping("/cart/items/delete")
	public String deleteSelectedItems(@RequestParam(value = "cartItemIds", required = false) List<Long> cartItemIds, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if(cartItemIds == null || cartItemIds.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "삭제할 상품을 선택하세요.");
			return "redirect:/cart";
		}
		
		cartService.deleteCartItems(cartItemIds);
		return "redirect:/cart";
	}
	
	// 장바구니 수량 수정 기능 ( 기능 다 만든 후, AJAX 비동기 방식으로 바꾸자. )
	@PostMapping("/cart/item/{cartItemId}/update")
	public String updateCartItem(@PathVariable Long cartItemId, @RequestParam int count) {
		cartService.updateCartItemCount(cartItemId, count);
		return "redirect:/cart";
	}
	
	@PostMapping("/cart/order/form")
	public String cartOrderForm(@RequestParam(value = "cartItemIds", required=false)List<Long> cartItemIds, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		Member loginMember = sessionManager.getLoginMember(request);
		if(loginMember == null) {
			return "redirect:/login";
		}
		
		// 전체주문이 아니라 선택 주문이라면?
		List<CartItem> cartItems;
		if(cartItemIds == null || cartItemIds.isEmpty()) {
			redirectAttributes.addFlashAttribute("errorMessage", "선택된 상품이 없습니다.");
		    return "redirect:/cart";
		}
		else
		{
			cartItems = cartItemRepository.findAllById(cartItemIds); 
		}
		
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
