package com.ljm.copang.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Review {
	@Id @GeneratedValue
	private Long id; 
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;
	
	private String content;					// 리뷰 내용
	private int rating;					// 별점
	
	private LocalDateTime createdTime;		// 생성된 시간
	
	// 리뷰 생성 메서드
	public static Review createReview(Member member, Item item, String content, int rating) {
		Review review = new Review();
		review.setMember(member);
		review.setItem(item);
		review.setContent(content);
		review.setRating(rating);
		review.setCreatedTime(LocalDateTime.now());
		return review;
	}
}
