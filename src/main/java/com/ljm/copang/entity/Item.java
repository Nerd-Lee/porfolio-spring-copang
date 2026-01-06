package com.ljm.copang.entity;

import com.ljm.copang.exception.NotEnoughStockException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Item {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_id")
	private Long id;						// 상품 번호
	
	private String name;					// 상품명
	private int price;						// 가격
	private int stockQuantity;				// 수량
	
	private String imageUrl; 				// 상품의 대표 이미지 경로
	
	@Lob
	@Column(columnDefinition = "TEXT")
	private String description;				// 상품 설명
	
	private int reviewCount = 0; 			// 리뷰의 갯수
	private double averageRating = 0.0; 	// 상품의 평균 평점
	
	// 수량 증가
	public void addStock(int quantity) {
		this.stockQuantity += quantity;
	}
	
	// 수량 감소
	public void removeStock(int quantity) {
		int restStock = this.stockQuantity - quantity;
		// 만약에 재고가 부족할 경우라면, 예외를 발생시켜야 한다.
		if(restStock < 0) {
			throw new NotEnoughStockException("상품의 재고가 부족합니다. (현재 재고: " + this.stockQuantity + "개)");
		}
		this.stockQuantity = restStock;
	}
	
	// 리뷰 평점 계산 및 수정, 그리고 리뷰갯수 추가
	public void addReviewRating(int newRating) {
		double totalRating = (this.averageRating * reviewCount) + newRating;
		this.reviewCount++;
		this.averageRating = totalRating / this.reviewCount;
	}
	
	// 현재 이 기능은 만들지는 않을거지만, 리뷰를 수정해서 rating을 수정한다면?
	public void editReviewRating(int beforeRating, int editRating) {
		double totalRating = (this.averageRating * reviewCount) - beforeRating + editRating;
		this.averageRating = totalRating / this.reviewCount;
	}
}