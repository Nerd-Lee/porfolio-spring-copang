package com.ljm.copang.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ljm.copang.entity.Cart;
import com.ljm.copang.entity.CartItem;
import com.ljm.copang.entity.Item;
import com.ljm.copang.entity.Member;
import com.ljm.copang.entity.Order;
import com.ljm.copang.entity.OrderItem;
import com.ljm.copang.enums.OrderStatus;
import com.ljm.copang.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

    private final OrderRepository orderRepository;
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ItemRepository itemRepository;
	private final MemberRepository memberRepository;
	
	// 카트를 추가하거나 카트에 동일한 상품이 담겨있는 지 확인 후 추가하는 기능
	public void addCart(Member member, Long itemId, int count) {
		// 회원의 장바구니가 있는가?
		Cart cart = cartRepository.findByMemberId(member.getId());
		
		// 한 번도 장바구니에 추가한 아이템이 없다면 장바구니를 생성해준다.
		if(cart == null) {
			cart = new Cart();
			cart.setMember(member);
			cartRepository.save(cart);
		}
		
		// 장바구니에 해당 상품이 담겨있다면?
		Item item = itemRepository.findById(itemId).orElseThrow();
		CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());
		
		// 만약 해당 상품이 담겨있지 않다면, 새로 추가해주고, 추가가 되어있는 상품이라면 수량만 증가시켜준다.
		if(savedCartItem == null) {
			savedCartItem = new CartItem();
			savedCartItem.setCart(cart);
			savedCartItem.setItem(item);
			savedCartItem.setCount(count);
			cartItemRepository.save(savedCartItem);
		}
		else{
			savedCartItem.setCount(savedCartItem.getCount() + count);
		}
	}
	
	// 장바구니 개별 상품 삭제
	public void deleteCartItem(Long cartItemId) {
		cartItemRepository.deleteById(cartItemId);
	}
	
	// 장바구니 상품 전체 삭제
	public void deleteCartAllItem() {
	}
	
	// 장바구니 수량 변경
	public void updateCartItemCount(Long cartItemId, int count) {
		// 수량 변경을 한 장바구니에 추가 된 아이템을 찾는다.
		CartItem cartItem = cartItemRepository.findById(cartItemId)
							.orElseThrow(()-> new RuntimeException("장바구니 항목을 찾을 수가 없습니다."));
		
		// 만약 아이템의 수량이 0이라면, 카트에서 해당 상품을 없앤다.
		// 근데 1이상이라면 해당 수량으로 수정한다.
		if(count <= 0) {
			cartItemRepository.delete(cartItem);
		}
		else {
			cartItem.setCount(count);
		}
	}
	
	public Long orderCartItems(Long memberId) {
		// 회원과 장바구니를 조회한다.
		Member member = memberRepository.findById(memberId).orElseThrow();
		Cart cart = cartRepository.findByMemberId(memberId);
		List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
		
		// 주문 상품 리스트 생성
		List<OrderItem> orderItems = new ArrayList<>();
		for (CartItem cartItem : cartItems) {
			// 장바구니 아이템 정보로 OrderItem 생성
			OrderItem orderItem = OrderItem.createOrderItem(cartItem.getItem(), cartItem.getItem().getPrice(), cartItem.getCount());
			orderItems.add(orderItem);
		}
		
		// 실제 주문 엔티티
		Order order = new Order();
		order.setMember(member);
		for(OrderItem oi : orderItems) {
			order.getOrderItems().add(oi);
			oi.setOrder(order);
		}
		order.setOrderDateTime(LocalDateTime.now());
		order.setStatus(OrderStatus.ORDER);
		
		orderRepository.save(order);
		cartItemRepository.deleteAll(cartItems);
		
		return order.getId();
	}
}