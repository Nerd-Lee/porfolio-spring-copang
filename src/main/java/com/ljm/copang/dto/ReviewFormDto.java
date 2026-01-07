package com.ljm.copang.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReviewFormDto {
	
	private Long itemId;							// 리뷰 작성할 아이템 Id
	private Long orderId;							// 주문 번호의 id
	private String itemName;						// 화면 표시용 상품명
	private String imageUrl;						// 리뷰 작성할 아이템의 이미지를 보여줄 url
	
	@NotBlank(message = "리뷰 내용을 입력해주세요.")
	private String content;							// 리뷰 작성할 내용
	
	@Min(1) @Max(5)
	private int rating;								// 별점
}