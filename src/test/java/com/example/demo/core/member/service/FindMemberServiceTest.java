package com.example.demo.core.member.service;

import com.example.demo.annotation.IntegrationTest;
import com.example.demo.core.member.domain.Member;
import com.example.demo.core.member.result.FindMemberResult;
import com.example.demo.infrastructure.persistence.member.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.demo.MemberFixtures.MEMBER_ID;
import static com.example.demo.MemberFixtures.MEMBER_NAME;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@IntegrationTest
@DisplayName("FindMemberServiceTest")
class FindMemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FindMemberService findMemberService;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }

    @Nested
    @DisplayName("findByMemberId 메소드는")
    class Describe_findByMemberId {

        final String memberId = MEMBER_ID;
        final String memberName = MEMBER_NAME;

        @Nested
        @DisplayName("Member 데이터가 존재하지 않으면")
        class Context_memberNotExist {

            @Test
            @DisplayName("IllegalArgumentException을 던진다.")
            void it() {
                assertThrows(IllegalArgumentException.class, () -> {
                    findMemberService.findByMemberId(memberId);
                });
            }
        }

        @Nested
        @DisplayName("Member 데이터가 존재하면")
        class Context_memberExist {

            @BeforeEach
            void before() {
                memberRepository.save(
                    new Member(memberId, memberName)
                );
            }

            @Test
            @DisplayName("Member를 리턴한다.")
            void it() {
                FindMemberResult result = findMemberService.findByMemberId(memberId);

                assertInstanceOf(FindMemberResult.class, result);
                assertNotNull(result);
            }
        }
    }
}
