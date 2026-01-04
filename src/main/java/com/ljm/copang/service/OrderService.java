package com.ljm.copang.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ljm.copang.dto.OrderFormDto;
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
	
	@Transactional
	public Long createOrder(Long memberId, String receiver, String address, String phoneNumber, List<OrderFormDto.OrderItemDto> itemDtos) {
		// 엔티티 조회
		Member member = memberRepository.findById(memberId).orElseThrow();
		
		List<OrderItem> orderItems = new ArrayList<>();
		for(OrderFormDto.OrderItemDto dto : itemDtos) {
			Item item = itemRepository.findById(dto.getItemId()).orElseThrow();
			OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), dto.getCount());
			orderItems.add(orderItem);
		}
		
		Order order = Order.createOrder(member, receiver, address, phoneNumber, orderItems);
		orderRepository.save(order);
		
		return order.getId();
	}
	
	public Order findOne(Long orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow();
		return order;
	}
	
	@Transactional
	public void cancelOrder(Long orderId) {
		Order order = orderRepository.findById(orderId)
					  .orElseThrow(()-> new RuntimeException("주문 정보를 찾을 수가 없습니다."));
		
		order.cancel();
	}
}