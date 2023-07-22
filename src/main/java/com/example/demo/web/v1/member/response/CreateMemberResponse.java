package com.example.demo.web.v1.member.response;

import com.example.demo.core.member.result.FindMemberResult;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

import static com.example.demo.common.constants.DateFormatConstants.ISO_8601;
import static com.example.demo.common.constants.DateFormatConstants.TIMEZONE;

@Getter
@Builder
public class CreateMemberResponse {

    private long memberNo;

    private String memberId;

    private String memberName;

    @JsonFormat(pattern = ISO_8601, timezone = TIMEZONE)
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
