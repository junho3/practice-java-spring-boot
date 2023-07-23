package com.example.demo.core.member.service;

import com.example.demo.annotation.IntegrationTest;
import com.example.demo.core.member.param.CreateMemberParam;
import com.example.demo.core.member.result.FindMemberResult;
import com.example.demo.infrastructure.persistence.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.demo.MemberFixtures.MEMBER_ID;
import static com.example.demo.MemberFixtures.MEMBER_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@IntegrationTest
class CreateMemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CreateMemberService createMemberService;

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

            private final String memberId = MEMBER_ID;
            private final String memberName = MEMBER_NAME;

            final CreateMemberParam param = new CreateMemberParam(memberId, memberName);

            @Test
            @DisplayName("Member를 생성한다.")
            void it() {
                FindMemberResult result = createMemberService.create(param);

                assertNotNull(result);
                assertEquals(result.getMemberId(), memberId);
                assertEquals(result.getMemberName(), memberName);
            }
        }
    }
}
