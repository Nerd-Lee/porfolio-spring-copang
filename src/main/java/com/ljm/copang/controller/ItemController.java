package com.ljm.copang.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ljm.copang.component.SessionManager;
import com.ljm.copang.entity.Item;
import com.ljm.copang.entity.Member;
import com.ljm.copang.repository.ItemRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

// 상품 상세 페이지 컨트롤러
@Controller
@RequiredArgsConstructor
public class ItemController {
	
	private final ItemRepository itemRepository;
	private final SessionManager sessionManager;
	
	// 클릭 한 물건의 대한 상세 정보 item 상세페이지로 넘기기
	@GetMapping("/items/{itemId}")
	public String itemDetail(@PathVariable long itemId, Model model) {
		Item item = itemRepository.findById(itemId).get();
		model.addAttribute("item", item);
		return "item/item_detail";
	}
	
	// 상품 수정 페이지
	@GetMapping("/items/{itemId}/edit")
	public String editForm(@PathVariable Long itemId, Model model, HttpServletRequest request) {
		Item item = itemRepository.findById(itemId)
					.orElseThrow(()-> new IllegalArgumentException("해당 상품이 없습니다. id=" + itemId));
		
		Member loginMember = sessionManager.getLoginMember(request);
		
		if(!item.getMember().getId().equals(loginMember.getId())) {
			return "redirect:/";
		}
		
		model.addAttribute("item", item);
		return "item/item_edit";
	}
	
	@PostMapping("/items/{itemId}/edit")
	public String edit(@PathVariable Long itemId, @ModelAttribute("item") Item form, HttpServletRequest request) {
		Item item = itemRepository.findById(itemId)
				.orElseThrow(()-> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));
		
		Member loginMember = sessionManager.getLoginMember(request);
		
		if(item.getMember() == null || !item.getMember().getId().equals(loginMember.getId())) {
			return "redirect:/";
		}
		
		item.setName(form.getName());
		item.setPrice(form.getPrice());
		item.setStockQuantity(form.getStockQuantity());
		item.setImageUrl(form.getImageUrl());
		item.setDescription(form.getDescription());
		
		itemRepository.save(item);
		
		return "redirect:/items/{itemId}";
	}
	
	// 상품 삭제 기능
	@PostMapping("/items/{itemId}/delete")
	public String itemDelete(@PathVariable Long itemId, HttpServletRequest request) {
		Item item = itemRepository.findById(itemId)
					.orElseThrow(()-> new RuntimeException("상품을 찾을 수 없습니다."));
		
		Member loginMember = sessionManager.getLoginMember(request);
		if(loginMember == null || !item.getMember().getId().equals(loginMember.getId())) {
			return "redirect:/items";
		}
		
		itemRepository.delete(item);
		
		return "redirect:/";
	}
	
	@GetMapping("/items/list/my")
	public String myAddItems(HttpServletRequest request, Model model) {
		Member loginMember = sessionManager.getLoginMember(request);
		if(loginMember == null) {
			return "redirect:/login";
		}
		
		List<Item> items = itemRepository.findByMemberId(loginMember.getId());
		model.addAttribute("items", items);
		
		return "item/my_add_items";
	}
	
	/*
	 * 등록 부분만 밑으로 따로 빼놓는 이유는, 경로의 시작이 등록만 /item으로 시작하기 때문
	 * 이유는 item 등록에 대한 부분은, 로그인하지 않으면 등록을 할 수 없어서, 인터셉터 방식으로 해야하는데
	 * items/** 이렇게 해놓게 된다면.. /item/add 도 로그인하지 않고도 이동할 수 있어서 헷갈려서 내가 따로 빼놓음
	 */
	
	// 상품 등록 화면
	@GetMapping("/item/add")
	public String addItemForm(Model model) {
		model.addAttribute("item", new Item());
		return "item/item_add";
	}
	
	// 상품 등록 후 DB 저장
	@PostMapping("/item/add")
	public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		// 세션에서 로그인 회원 정보를 꺼내서, 등록된 상품에 현재 회원이 등록했다는 member_id를 추가해주는 작업
		Member loginMember = sessionManager.getLoginMember(request);
		
		item.setMember(loginMember);
		
		Item savedItem = itemRepository.save(item);
		
		// 상품 저장 후, 저장 한 상품 상세페이지로 이동하기
		redirectAttributes.addAttribute("itemId", savedItem.getId());
		redirectAttributes.addAttribute("status", true);
		return "redirect:/items/{itemId}";
	}
}
