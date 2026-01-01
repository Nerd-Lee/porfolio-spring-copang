package com.ljm.copang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ljm.copang.entity.Item;
import com.ljm.copang.repository.ItemRepository;
import com.ljm.copang.service.MemberService;

import lombok.RequiredArgsConstructor;

// 상품 상세 페이지 컨트롤러
@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
	
	private final ItemRepository itemRepository; 
	
	// 클릭 한 물건의 대한 상세 정보 item 상세페이지로 넘기기
	@GetMapping("/{itemId}")
	public String itemDetail(@PathVariable long itemId, Model model) {
		Item item = itemRepository.findById(itemId).get();
		model.addAttribute("item", item);
		return "item/item_detail";
	}
	
	// 상품 등록 화면
	@GetMapping("/add")
	public String addItemForm(Model model) {
		model.addAttribute("item", new Item());
		return "item/item_add";
	}
	
	// 상품 등록 후 DB 저장
	@PostMapping("/add")
	public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
		Item savedItem = itemRepository.save(item);
		
		// 상품 저장 후, 저장 한 상품 상세페이지로 이동하기
		redirectAttributes.addAttribute("itemId", savedItem.getId());
		redirectAttributes.addAttribute("status", true);
		return "redirect:/items/{itemId}";
	}
}
