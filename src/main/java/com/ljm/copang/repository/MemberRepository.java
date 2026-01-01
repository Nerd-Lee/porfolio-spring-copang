package com.ljm.copang.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ljm.copang.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{
	// 이메일로 회원 찾기 같은 기능
	Optional<Member> findByEmail(String email);
}