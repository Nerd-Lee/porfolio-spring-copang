package com.ljm.copang.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.ljm.copang.entity.Item;
import com.ljm.copang.entity.Member;
import com.ljm.copang.entity.Order;
import com.ljm.copang.entity.OrderItem;
import com.ljm.copang.enums.OrderStatus;
import com.ljm.copang.repository.ItemRepository;
import com.ljm.copang.repository.MemberRepository;
import com.ljm.copang.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
	private final OrderRepository orderRepository;
	private final MemberRepository memberRepository;
	private final ItemRepository itemRepository;
	
	public Long order(Long memberId, Long itemId, int count) {
		// 엔티티 조회
		Member member = memberRepository.findById(memberId).orElseThrow();
		Item item = itemRepository.findById(itemId).orElseThrow();
		
		OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
		
		Order order = new Order();
		order.setMember(member);
		order.getOrderItems().add(orderItem);
		orderItem.setOrder(order);
		order.setOrderDateTime(LocalDateTime.now());
		order.setStatus(OrderStatus.CANCEL);
		
		orderRepository.save(order);
		
		return order.getId();
	}
	
	@Transactional
	public void cancelOrder(Long orderId) {
		Order order = orderRepository.findById(orderId)
					  .orElseThrow(()-> new RuntimeException("주문 정보를 찾을 수가 없습니다."));
		
		order.cancel();
	}
}