package com.ljm.copang.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ljm.copang.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Cart findByMemberId(Long memberId);
}
