package com.ljm.copang.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PasswordUpdateDto {
	@NotEmpty(message = "현재 비밀번호를 입력해주세요.")
	private String curPassword;
	
	@NotEmpty(message = "새 비밀번호를 입력해주세요.")
	@Length(min = 4, max = 16, message = "비밀번호는 4자 이상, 16자 이하로 입력해주세요.")
	private String newPassword;
	
	@NotEmpty(message = "수정하실 새 비밀번호를 한 번 더 입력하세요.")
	private String newConfirmPassword;
}
