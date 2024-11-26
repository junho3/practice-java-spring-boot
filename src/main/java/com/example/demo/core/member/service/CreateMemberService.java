package com.example.demo.core.member.service;

import com.example.demo.core.member.domain.Member;
import com.example.demo.core.member.event.CreateMemberEvent;
import com.example.demo.core.member.param.CreateMemberParam;
import com.example.demo.core.member.result.FindMemberResult;
import com.example.demo.infrastructure.persistence.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateMemberService {

    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public FindMemberResult create(CreateMemberParam param) {
        final Member member = memberRepository.save(
            new Member(
                param.getMemberId(),
                param.getMemberName()
            )
        );

        applicationEventPublisher.publishEvent(CreateMemberEvent.from(member));

        return FindMemberResult.from(member);
    }
}
