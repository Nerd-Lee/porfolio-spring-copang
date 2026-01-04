package com.ljm.copang.service;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ljm.copang.dto.ItemSearchDto;
import com.ljm.copang.entity.Item;
import com.ljm.copang.repository.ItemRepository;
import com.ljm.copang.specification.ItemSpecification;

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
	
	// 상품명, 가격 조건, 재고 있는 지 검색 후 상품명에 맞는 아이템 데이터 반환
	public List<Item> searchItems(ItemSearchDto searchDto){
		Specification<Item> spec = Specification.where(ItemSpecification.equalName(searchDto.getSearchQuery()))
								   .and(ItemSpecification.greaterThanOrEqualToPrice(searchDto.getMinPrice()))
								   .and(ItemSpecification.lessThanOrEqualToPrice(searchDto.getMaxPrice()))
								   .and(ItemSpecification.onlyInStock(searchDto.getOnlyItemsInStock()));
		
		return itemRepository.findAll(spec);
	}
}