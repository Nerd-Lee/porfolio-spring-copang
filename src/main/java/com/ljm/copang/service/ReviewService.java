package com.ljm.copang.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ljm.copang.dto.ReviewFormDto;
import com.ljm.copang.entity.Item;
import com.ljm.copang.entity.Member;
import com.ljm.copang.entity.Order;
import com.ljm.copang.entity.Review;
import com.ljm.copang.enums.OrderStatus;
import com.ljm.copang.repository.ItemRepository;
import com.ljm.copang.repository.MemberRepository;
import com.ljm.copang.repository.OrderRepository;
import com.ljm.copang.repository.ReviewRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
	
	private final ItemRepository itemRepository;
	private final MemberRepository memberRepository;
	private final ReviewRepository reviewRepository;
	private final OrderRepository orderRepository;
	
	// 리뷰 저장 로직
	@Transactional
	public Long saveReview(ReviewFormDto dto, Long memberId) {
		Member member = memberRepository.findById(memberId).orElseThrow(()-> new IllegalStateException("멤버의 정보가 없습니다."));;
		Item item = itemRepository.findById(dto.getItemId()).orElseThrow(()-> new IllegalStateException("상품이 없습니다."));
		
		Order order = orderRepository.findById(dto.getOrderId()).orElseThrow(()-> new IllegalStateException("주문 정보를 찾을 수 없습니다."));
		order.setStatus(OrderStatus.REVIEW_COMPLETED);
		
		Review review = Review.createReview(member, item, dto.getContent(), dto.getRating());
		reviewRepository.save(review);
		
		// 아이템의 리뷰갯수와 평균 평점을 계산해서 저장한다.
		item.addReviewRating(dto.getRating());
		
		return review.getId();
	}
	
	public List<Review> findReviewsByItem(Long itemId){
		return reviewRepository.findByItemIdOrderByCreatedTimeDesc(itemId);
	}
}
