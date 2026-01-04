package com.ljm.copang.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ljm.copang.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {
	// Where절을 사용해서, memberId와 맞는 아이템을 전부 가져온다.
	List<Item> findByMemberId(Long memberId);
	
	// 검색어가 상품명에 포함되어 있다면, 해당 상품을 찾아서 가져온다.
	List<Item> findByNameContainingIgnoreCase(String name);
}