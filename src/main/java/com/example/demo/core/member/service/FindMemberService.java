package com.example.demo.core.member.service;

import com.example.demo.config.persistence.ReadTransactional;
import com.example.demo.core.member.domain.Member;
import com.example.demo.core.member.result.FindMemberResult;
import com.example.demo.infrastructure.persistence.member.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class FindMemberService {

    private final MemberRepository memberRepository;

    public FindMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @ReadTransactional
    public FindMemberResult findByMemberId(String memberId) {
        Member member = memberRepository.findByMemberId(memberId)
            .orElseThrow(IllegalArgumentException::new);

        return FindMemberResult.from(member);
    }
}
