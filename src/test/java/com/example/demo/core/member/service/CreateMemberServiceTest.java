package com.example.demo.core.member.service;

import com.example.demo.TestFixtures;
import com.example.demo.annotation.IntegrationTest;
import com.example.demo.core.member.param.CreateMemberParam;
import com.example.demo.core.member.result.FindMemberResult;
import com.example.demo.infrastructure.persistence.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.example.demo.MemberFixtures.EMAIL;
import static com.example.demo.MemberFixtures.MEMBER_ID;
import static com.example.demo.MemberFixtures.MEMBER_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CreateMemberService")
@IntegrationTest
@RequiredArgsConstructor
class CreateMemberServiceTest {

    private final MemberRepository memberRepository;
    private final CreateMemberService createMemberService;

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

            final CreateMemberParam param = TestFixtures.get().giveMeBuilder(CreateMemberParam.class)
                .set("memberId", MEMBER_ID)
                .set("memberName", MEMBER_NAME)
                .set("email", EMAIL)
                .sample();

            @Test
            @DisplayName("Member를 생성한다.")
            void it() {
                final FindMemberResult actual = createMemberService.create(param);

                assertThat(actual).isNotNull();
                assertThat(actual.getMemberId()).isEqualTo(MEMBER_ID);
                assertThat(actual.getMemberName()).isEqualTo(MEMBER_NAME);
                assertThat(actual.getEmail()).isEqualTo(EMAIL);
            }
        }
    }
}
