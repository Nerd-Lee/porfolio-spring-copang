package com.ljm.copang.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ljm.copang.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
	// Where절을 사용해서, memberId와 맞는 아이템을 전부 가져온다.
	List<Item> findByMemberId(Long memberId);
}