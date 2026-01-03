package com.ljm.copang.testdb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.ljm.copang.entity.Item;
import com.ljm.copang.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InitDB implements CommandLineRunner{
	private final ItemRepository itemRepository;
	
	/*
	public void run(String... args) throws Exception {
	
	}*/
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
		// 테스트용 상품
		Item item1 = new Item();
		item1.setName("코팡 추천 티셔츠");
		item1.setPrice(15000);
		item1.setStockQuantity(100);
		item1.setDescription("가성비 최고의 면 티셔츠, 수피마 원단을 사용");
		itemRepository.save(item1);
		
		Item item2 = new Item();
		item2.setName("맥북프로 32인치");
		item2.setPrice(5000000);
		item2.setStockQuantity(50);
		item2.setDescription("작은화면으로 고생하는 개발자를 위한 맥북 32인치, 하지만 외부작업은 불가능합니다.");
		itemRepository.save(item2);
	}
}
