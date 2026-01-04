package com.ljm.copang.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ljm.copang.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	CartItem findByCartIdAndItemId(Long cartId, Long itemId);
	List<CartItem> findByCartId(Long cartId);
}
