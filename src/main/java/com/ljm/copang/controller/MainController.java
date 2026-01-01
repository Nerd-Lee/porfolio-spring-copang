package com.ljm.copang.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ljm.copang.entity.Item;
import com.ljm.copang.service.ItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	private final ItemService itemService;
	
	// 모든 상품을 가져와서 'items' 라는 이름으로 화면에 전달하는 역할
	@GetMapping
	public String home(Model model) {
		List<Item> items = itemService.findItems();
		model.addAttribute("items", items);
		return "home";
	}
}
