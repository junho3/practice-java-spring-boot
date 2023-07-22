package com.example.demo.core.member.service;

import com.example.demo.core.member.domain.Member;
import com.example.demo.core.member.param.CreateMemberParam;
import com.example.demo.core.member.result.FindMemberResult;
import com.example.demo.infrastructure.persistence.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateMemberService {

    private final MemberRepository memberRepository;

    public CreateMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public FindMemberResult create(CreateMemberParam param) {
        final Member member = memberRepository.save(
            new Member(
                param.getMemberId(),
                param.getMemberName()
            )
        );

        return FindMemberResult.from(member);
    }
}
