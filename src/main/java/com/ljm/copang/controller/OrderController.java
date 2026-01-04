package com.ljm.copang.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ljm.copang.component.SessionManager;
import com.ljm.copang.dto.OrderFormDto;
import com.ljm.copang.entity.Item;
import com.ljm.copang.entity.Member;
import com.ljm.copang.entity.Order;
import com.ljm.copang.exception.NotEnoughStockException;
import com.ljm.copang.repository.ItemRepository;
import com.ljm.copang.repository.OrderRepository;
import com.ljm.copang.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OrderController {
	
	private final OrderService orderService;
	private final SessionManager sessionManager;
	private final OrderRepository orderRepository;
	private final ItemRepository itemRepository;
	
	// 주문 내역 페이지
	@GetMapping("/orders")
	public String orderListView(HttpServletRequest request, Model model) {
		Member loginMember = sessionManager.getLoginMember(request);
		if(loginMember == null) {
			return "redirect:/login";
		}
		
		List<Order> orders = orderRepository.findByMemberId(loginMember.getId());
		
		model.addAttribute("orders", orders);
		
		return "order/order_list_view";
	}
	
	@GetMapping("/orders/{orderId}")
	public String orderDetail(@PathVariable("orderId") Long orderId, HttpServletRequest request, Model model) {
		Member loginMember = sessionManager.getLoginMember(request);
		if(loginMember == null) {
			return "redirect:/login";
		}
		
		Order order = orderService.findOne(orderId);
		
		if(!order.getMember().getId().equals(loginMember.getId())) {
			return "redirect:/orders";
		}
		
		model.addAttribute("order", order);
		return "order/order_detail_view";
	}
	
	// 주문취소 기능
	@PostMapping("/orders/{orderId}/cancel")
	public String cancelOrder(@PathVariable Long orderId) {
		orderService.cancelOrder(orderId);
		return "redirect:/orders";
	}
	
	// 단건 주문에 대한 기능
	@PostMapping("/order/form")
	public String orderForm(@RequestParam Long itemId, @RequestParam int count,
							HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		Member loginMember = sessionManager.getLoginMember(request);
		if(loginMember == null) {
			return "redirect:/login";
		}
		
		Item item = itemRepository.findById(itemId).orElseThrow();
		
		// 여기서 먼저 체크! (결제 시 사용하는 로직 재활용)
	    if (item.getStockQuantity() < count) {
	    	redirectAttributes.addFlashAttribute("errorMessage", "재고가 부족하여 주문 할 수 없습니다.");
	    	return "redirect:/items/" + itemId;
	    }
		
		OrderFormDto form = new OrderFormDto();
		
		OrderFormDto.OrderItemDto itemDto = new OrderFormDto.OrderItemDto(
	            item.getId(),
	            item.getName(),
	            item.getPrice(),
	            count,
	            item.getImageUrl()
	    );
		
	    form.getOrderItems().add(itemDto);
	    
	    // 배송 기본 정보 및 총액 세팅
	    form.setReceiverName(loginMember.getName());
	    form.setTotalPrice(item.getPrice() * count);
		
		model.addAttribute("orderForm", form);
		return "order/order_form_view";
	}
	
	// 주문페이지에서 결제시 기능
	@PostMapping("/order/form/process")
	public String processOrder(@ModelAttribute OrderFormDto form, HttpServletRequest request, Model model) {
		Member loginMember = sessionManager.getLoginMember(request);
		if(loginMember == null) {
			return "redirect:/login";
		}
	
		try {
			orderService.createOrder(loginMember.getId(), form.getReceiverName(), form.getAddress(), form.getPhoneNumber(), form.getOrderItems());
		}
		catch(NotEnoughStockException e) {
			int total = 0;
			
		    for (OrderFormDto.OrderItemDto itemDto : form.getOrderItems()) {
		        Item item = itemRepository.findById(itemDto.getItemId()).orElseThrow();
		        itemDto.setItemName(item.getName());
		        itemDto.setItemPrice(item.getPrice());
		        total += item.getPrice() * itemDto.getCount();
		    }
		    
		    form.setTotalPrice(total);
			
			model.addAttribute("errorMessage", e.getMessage());
			model.addAttribute("orderForm", form);
			return "order/order_form_view";
		}
		
		return "redirect:/orders";
	}
}
