package com.ljm.copang.exception;

// 재고 부족 시 발생 예외처리 클래스
public class NotEnoughStockException extends RuntimeException {
	public NotEnoughStockException(String message) {
		super(message);
	}
	
	public NotEnoughStockException(String message, Throwable cause) {
		super(message, cause);
	}
}
