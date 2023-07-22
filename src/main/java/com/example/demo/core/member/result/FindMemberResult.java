package com.example.demo.core.member.result;

import com.example.demo.core.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FindMemberResult {

    private long memberNo;

    private String memberId;

    private String memberName;

    private LocalDateTime createdAt;

    public static FindMemberResult from(Member member) {
        return FindMemberResult.builder()
            .memberNo(member.getMemberNo())
            .memberId(member.getMemberId())
            .memberName(member.getMemberName())
            .createdAt(member.getCreatedAt())
            .build();
    }
}
