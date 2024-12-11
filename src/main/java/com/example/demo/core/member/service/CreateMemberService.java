package com.example.demo.core.member.service;

import com.example.demo.core.member.domain.Member;
import com.example.demo.core.member.event.CreateMemberEvent;
import com.example.demo.core.member.param.CreateMemberParam;
import com.example.demo.core.member.result.FindMemberResult;
import com.example.demo.infrastructure.persistence.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateMemberService {

    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public FindMemberResult create(CreateMemberParam param) {

        // micrometer traceId, spanId 확인을 위한 임시 로그
        log.info("Start processing request");

        final Member member = memberRepository
            .save(new Member(param.memberId(), param.memberName(), param.email()));

        applicationEventPublisher.publishEvent(CreateMemberEvent.from(member));

        return FindMemberResult.from(member);
    }
}
