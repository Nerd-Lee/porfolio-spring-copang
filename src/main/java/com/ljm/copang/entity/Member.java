package com.ljm.copang.entity;

import com.ljm.copang.dto.MemberJoinDto;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;
	
	@Column(unique = true, nullable = false)
	private String email;
	
	private String password;
	private String name;
	
	//@Embedded
	//private Address address;
	
	//@Enumerated(EnumType.STRING)
	//private Role role;
	
	/*public void updateProfile(String name, Address address) {
		this.name = name;
		this.address = address;
	}*/
	
	public static Member createMember(MemberJoinDto form) {
		Member member = new Member();
		member.email = form.getEmail();
		member.password = form.getPassword();
		member.name = form.getName();
		return member;
	}
}
