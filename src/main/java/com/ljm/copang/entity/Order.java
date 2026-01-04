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
	
	public void cancel() {
		this.status = OrderStatus.CANCEL;
		for(OrderItem orderItem : orderItems) {
			orderItem.cancel();
		}
	}
}
