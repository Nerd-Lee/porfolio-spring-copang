package com.ljm.copang.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ljm.copang.enums.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {
	@Id @GeneratedValue
	@Column(name = "order_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	
	// 주문 상품들 (주문 하나에 여러 상품)
    // cascade = CascadeType.ALL: Order만 저장해도 OrderItem까지 자동으로 같이 저장해줌
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> orderItems = new ArrayList<>();
	
	private LocalDateTime orderDateTime;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	private String receiverName;
	private String address;
	private String phoneNumber;
	private int totalPrice;
	
	// OrderItem 테이블과 연관관계 편의 메서드
	public void addOrderItem(OrderItem orderItem) {
		orderItems.add(orderItem);
		orderItem.setOrder(this);
	}
	
	// 주문 생성 메서드
	public static Order createOrder(Member member, String receiverName, String address, String phoneNumber, List<OrderItem> orderItems) {
		Order order = new Order();
		order.setMember(member);
		order.setReceiverName(receiverName);
		order.setAddress(address);
		order.setPhoneNumber(phoneNumber);
		
		int total = 0;
		for(OrderItem oi : orderItems) {
			order.addOrderItem(oi);
			total += (oi.getOrderPrice() * oi.getCount());
		}
		order.setTotalPrice(total);
		
		order.setOrderDateTime(LocalDateTime.now());
		order.setStatus(OrderStatus.ORDER);
		return order;
	}
	
	public void cancel() {
		this.status = OrderStatus.CANCEL;
		for(OrderItem orderItem : orderItems) {
			orderItem.cancel();
		}
	}
}
