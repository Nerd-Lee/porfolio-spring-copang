package com.ljm.copang.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ljm.copang.entity.Item;
import com.ljm.copang.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
	private final ItemRepository itemRepository;
	
	// 상품 등록 ( 저장 )
	@Transactional
	public Long saveItem(Item item) {
		itemRepository.save(item);
		return item.getId();
	}
	
	// 전체 상품 조회
	public List<Item> findItems(){
		return itemRepository.findAll();
	}
	
	public Item findOne(Long itemId) {
		return itemRepository.findById(itemId).orElseThrow(
				() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
	}
	
	// 상품명 검색 후 상품명에 맞는 아이템 데이터 반환
	public List<Item> searchItems(String keyword){
		if(keyword == null || keyword.isEmpty()) {
			return itemRepository.findAll();
		}
		return itemRepository.findByNameContainingIgnoreCase(keyword);
	}
}