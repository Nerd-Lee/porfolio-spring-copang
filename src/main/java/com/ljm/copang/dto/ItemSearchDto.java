package com.ljm.copang.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemSearchDto {
	private String searchQuery;			// 상품명 검색어
	private Integer minPrice;			// 최소 가격
	private Integer maxPrice;			// 최대 가격
	private Boolean onlyItemsInStock;	// 재고 있는 상품만 보기 true/false
}
