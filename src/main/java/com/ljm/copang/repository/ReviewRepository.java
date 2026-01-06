package com.ljm.copang.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ljm.copang.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	List<Review> findByItemId(Long itemId);
	List<Review> findByItemIdOrderByCreatedTimeDesc(Long itemId);
}
