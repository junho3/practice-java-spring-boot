package com.example.demo.web.v1.member.response;

import com.example.demo.core.member.domain.Member;
import com.example.demo.core.member.result.FindMemberResult;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class CreateMemberResponse {

    private long memberNo;

    private String memberId;

    private String memberName;

    private LocalDateTime createdAt;

    public static CreateMemberResponse from(FindMemberResult result) {
        return CreateMemberResponse.builder()
            .memberNo(result.getMemberNo())
            .memberId(result.getMemberId())
            .memberName(result.getMemberName())
            .createdAt(result.getCreatedAt())
            .build();
    }
}
