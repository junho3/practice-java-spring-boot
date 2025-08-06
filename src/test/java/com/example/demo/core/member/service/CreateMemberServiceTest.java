package com.example.demo.core.member.service;

import com.example.demo.TestFixtures;
import com.example.demo.annotation.IntegrationTest;
import com.example.demo.core.member.event.CreateMemberEvent;
import com.example.demo.core.member.param.CreateMemberParam;
import com.example.demo.core.member.result.FindMemberResult;
import com.example.demo.infrastructure.persistence.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CreateMemberService")
@IntegrationTest
@RecordApplicationEvents
@RequiredArgsConstructor
class CreateMemberServiceTest {

    private final MemberRepository memberRepository;
    private final CreateMemberService createMemberService;
    @Autowired
    private ApplicationEvents applicationEvents;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {

        @Nested
        @DisplayName("CreateMemberParam이 주어지면")
        class Context {

            final CreateMemberParam param = TestFixtures.get().giveMeOne(CreateMemberParam.class);

            @Test
            @DisplayName("Member를 생성하고 이벤트를 발행한다.")
            void it() {
                final FindMemberResult actual = createMemberService.create(param);

                assertThat(actual.getMemberId()).isEqualTo(param.memberId());
                assertThat(actual.getMemberName()).isEqualTo(param.memberName());
                assertThat(actual.getEmail()).isEqualTo(param.email());
                assertThat(applicationEvents.stream(CreateMemberEvent.class).count()).isEqualTo(1);
            }
        }
    }
}
