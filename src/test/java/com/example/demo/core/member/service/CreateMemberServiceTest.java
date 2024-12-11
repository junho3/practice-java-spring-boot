package com.example.demo.core.member.service;

import com.example.demo.annotation.IntegrationTest;
import com.example.demo.core.member.param.CreateMemberParam;
import com.example.demo.core.member.result.FindMemberResult;
import com.example.demo.infrastructure.persistence.member.MemberRepository;
import com.navercorp.fixturemonkey.FixtureMonkey;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.demo.MemberFixtures.EMAIL;
import static com.example.demo.MemberFixtures.MEMBER_ID;
import static com.example.demo.MemberFixtures.MEMBER_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@IntegrationTest
@DisplayName("CreateMemberService")
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

            private final CreateMemberParam param = FixtureMonkey.create()
                .giveMeBuilder(CreateMemberParam.class)
                .set("memberId", MEMBER_ID)
                .set("memberName", MEMBER_NAME)
                .set("email", EMAIL)
                .sample();

            @Test
            @DisplayName("Member를 생성한다.")
            void it() {
                final FindMemberResult actual = createMemberService.create(param);

                assertNotNull(actual);
                assertEquals(MEMBER_ID, actual.getMemberId());
                assertEquals(MEMBER_NAME, actual.getMemberName());
                assertEquals(EMAIL, actual.getEmail());
            }
        }
    }
}
