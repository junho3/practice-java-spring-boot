package com.example.demo.infrastructure.persistence;

import com.example.demo.core.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
