package com.ljm.copang.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
public class OrderFormDto {
	// 배송 정보
	private String receiverName;
	private String address;
	private String phoneNumber;
	
	// 주문한 상품 리스트
	private List<OrderItemDto> orderItems = new ArrayList<>();
	
	private int totalPrice;		// 전체 결제 금액
	
	@Getter @Setter
	@AllArgsConstructor
	@NoArgsConstructor
	// 주문 상품이 개별 상품일 수도 있고, 여러 상품 리스트일 수도 있기 때문에, 내부에 클래스를 하나 만든다.
	public static class OrderItemDto{
		private Long ItemId;
		private String itemName;
		private int itemPrice;
		private int count;
		private String imageUrl;		// 이미지 경로
	}
}
