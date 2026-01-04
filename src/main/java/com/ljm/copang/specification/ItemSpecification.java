package com.ljm.copang.specification;

import org.springframework.data.jpa.domain.Specification;

import com.ljm.copang.entity.Item;

public class ItemSpecification {
	// 상품명이 포함되어 있는지
	public static Specification<Item> equalName(String searchQuery){
		return (root, query, cb) ->
			   (searchQuery == null || searchQuery.isEmpty()) ? null : cb.like(root.get("name"), "%" + searchQuery + "%");
	}
	
	// 최소 가격 이상 조건
	public static Specification<Item> greaterThanOrEqualToPrice(Integer minPrice){
		return (root, query, cb) ->
			   (minPrice == null) ? null : cb.greaterThanOrEqualTo(root.get("price"), minPrice);
	}
	
	// 최대 가격 이하 조건
	public static Specification<Item> lessThanOrEqualToPrice(Integer maxPrice){
		return (root, query, cb) ->
			   (maxPrice == null) ? null : cb.lessThanOrEqualTo(root.get("price"), maxPrice);
	}
	
	public static Specification<Item> onlyInStock(Boolean onlyInStock){
		return (root, query, cb) ->
			   (onlyInStock == null || !onlyInStock) ? null : cb.greaterThan(root.get("stockQuantity"), 0);
	}
}
