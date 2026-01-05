package com.ljm.copang.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ljm.copang.dto.MemberJoinDto;
import com.ljm.copang.dto.PasswordUpdateDto;
import com.ljm.copang.entity.Member;
import com.ljm.copang.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	
	@Transactional
	public Long join(MemberJoinDto dto) {
		Member member = Member.createMember(dto);
		memberRepository.save(member);
		return member.getId();
	}
	
	public Member login(String email, String password) {
		// 비번이 일치하면 멤버 엔티티를 반환하고 틀리면 Null을 반환한다.
		return memberRepository.findByEmail(email).
				filter(m -> m.getPassword().equals(password))
				.orElse(null);
	}
	
	@Transactional
	public void updatePassword(Long memberId, PasswordUpdateDto dto) {
		// 회원을 먼저 찾는다.
		Member member = memberRepository.findById(memberId)
						.orElseThrow(()-> new IllegalStateException("회원이 존재하지 않습니다."));
		
		// 회원이 있다면, 비밀번호가 일치한 지 확인
		if(!dto.getCurPassword().equals(member.getPassword())) {
			throw new IllegalStateException("현재 비밀번호가 일치하지 않습니다.");
		}
		
		if(!dto.getNewPassword().equals(dto.getNewConfirmPassword())) {
			throw new IllegalStateException("새 비밀번호가 일치하지 않습니다.");
		}
		
		member.setPassword(dto.getNewPassword());
		memberRepository.save(member);
	}
}
