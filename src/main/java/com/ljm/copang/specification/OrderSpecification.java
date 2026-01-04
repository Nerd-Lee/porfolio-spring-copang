package com.ljm.copang.specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.ljm.copang.entity.Order;
import com.ljm.copang.entity.OrderItem;
import com.ljm.copang.enums.OrderStatus;

import jakarta.persistence.criteria.Join;

public class OrderSpecification {
	// 주문 상태 필터
	public static Specification<Order> equalStatus(String status){
		return (root, query, cb) ->
			   (status == null || status.isEmpty()) ? null : cb.equal(root.get("status"), OrderStatus.valueOf(status));
	}
	
	// 상품명 포함 필터
	public static Specification<Order> itemNameEqual(String itemName){
		return (root, query, cb) -> {
			if(itemName == null || itemName.isEmpty()) {
				return null;
			}
			Join<Order, OrderItem> orderItems = root.join("orderItems");
			return cb.like(orderItems.get("item").get("name"), "%" + itemName + "%");
		};
	}
	
	// 시작 날짜 필터
	public static Specification<Order> greaterThanOrEqualToDate(String startDate){
		return (root, query, cb) -> {
			if(startDate == null || startDate.isEmpty()) {
				return null;
			}
			LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
			return cb.greaterThanOrEqualTo(root.get("orderDateTime"), start);
		};
	}
	
	// 마지막 날짜 필터
	public static Specification<Order> lessThanOrEqualToDate(String endDate){
		return (root, query, cb) -> {
			if(endDate == null || endDate.isEmpty()) {
				return null;
			}
			LocalDateTime end = LocalDate.parse(endDate).atTime(23,59,59);
			return cb.lessThanOrEqualTo(root.get("orderDateTime"), end);
		};
	}
}