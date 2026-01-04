package com.ljm.copang.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ljm.copang.entity.Item;
import com.ljm.copang.entity.Member;
import com.ljm.copang.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
	public List<Order> findByMemberId(Long memberId);
}
