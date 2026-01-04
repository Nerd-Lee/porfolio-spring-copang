package com.ljm.copang.dto;

import java.time.LocalDateTime;

import com.ljm.copang.enums.OrderStatus;

import lombok.Getter;
import lombok.Setter;

// 주문 내역 조회 데이터를 저장할 DTO
@Getter @Setter
public class OrderSearchDto {
	// 주문 상태 검색 조건
	private String orderStatus;
	// 상품명
	private String itemName;
	// 조회 시작일
	private String startDate;
	// 조회 종료일
	private String endDate;
	// 정렬 조건
	private String sortOrder = "desc";
}
