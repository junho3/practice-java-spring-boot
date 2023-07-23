package com.example.demo.web.v1.member.response;

import com.example.demo.core.member.result.FindMemberResult;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

import static com.example.demo.common.constants.DateFormatConstants.ISO_8601;
import static com.example.demo.common.constants.DateFormatConstants.TIMEZONE;

@Getter
public class CreateMemberResponse {

    private final long memberNo;

    private final String memberId;

    private final String memberName;

    @JsonFormat(pattern = ISO_8601, timezone = TIMEZONE)
    private final LocalDateTime createdAt;

    public CreateMemberResponse(long memberNo, String memberId, String memberName, LocalDateTime createdAt) {
        this.memberNo = memberNo;
        this.memberId = memberId;
        this.memberName = memberName;
        this.createdAt = createdAt;
    }

    public static CreateMemberResponse from(FindMemberResult result) {
        return new CreateMemberResponse(
            result.getMemberNo(),
            result.getMemberId(),
            result.getMemberName(),
            result.getCreatedAt()
        );
    }
}
