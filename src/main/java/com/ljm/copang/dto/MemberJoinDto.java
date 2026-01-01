package com.ljm.copang.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

// 사용자가 입력한 값을 담는 DTO
@Getter @Setter
public class MemberJoinDto {
	@NotBlank(message = "이메일은 필수로 입력하셔야 합니다.")
	@Email(message = "올바른 이메일 형식이 아닙니다!")
	private String email;
	
	@NotBlank(message = "비밀번호를 입력하지 않으셨습니다.")
	@Size(min = 4, max = 10, message = "비밀번호는 최소 4자에서 최대 10자까지 입력 가능합니다.")
	private String password;
	
	private String name;
	private String city;
	private String street;
}