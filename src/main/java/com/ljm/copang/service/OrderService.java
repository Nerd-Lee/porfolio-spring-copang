package com.ljm.copang.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ljm.copang.dto.OrderFormDto;
import com.ljm.copang.dto.OrderSearchDto;
import com.ljm.copang.entity.Item;
import com.ljm.copang.entity.Member;
import com.ljm.copang.entity.Order;
import com.ljm.copang.entity.OrderItem;
import com.ljm.copang.enums.OrderStatus;
import com.ljm.copang.repository.ItemRepository;
import com.ljm.copang.repository.MemberRepository;
import com.ljm.copang.repository.OrderRepository;
import com.ljm.copang.specification.OrderSpecification;

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
	
	// 주문 내역 상세보기에 대한 데이터를 가져와 반환
	public Order findOne(Long orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow();
		return order;
	}
	
	public List<Order> getOrderList(OrderSearchDto searchDto){
		// 주문내역 검색 조건 조립
		Specification<Order> spec = Specification.where(OrderSpecification.equalStatus(searchDto.getOrderStatus()))
									.and(OrderSpecification.itemNameEqual(searchDto.getItemName()))
									.and(OrderSpecification.greaterThanOrEqualToDate(searchDto.getStartDate()))
									.and(OrderSpecification.lessThanOrEqualToDate(searchDto.getEndDate()));
		
		// 최신 or 오래된순 정렬
		Sort sort;
		if("asc".equals(searchDto.getSortOrder())) {
			sort = Sort.by(Sort.Direction.ASC, "orderDateTime");		// 오래된 순 정렬
		}
		else {
			sort = Sort.by(Sort.Direction.DESC, "orderDateTime");		// 최신순 정렬
		}
		
		return orderRepository.findAll(spec, sort);
	}
	
	@Transactional
	public void cancelOrder(Long orderId) {
		Order order = orderRepository.findById(orderId)
					  .orElseThrow(()-> new RuntimeException("주문 정보를 찾을 수가 없습니다."));
		
		order.cancel();
	}
	
	@Transactional
	public void compleateOrder(Long orderId) {
		Order order = orderRepository.findById(orderId).get();
		order.setStatus(OrderStatus.COMPLETED);
	}
}