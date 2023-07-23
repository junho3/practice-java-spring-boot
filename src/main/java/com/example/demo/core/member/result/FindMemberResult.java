package com.example.demo.core.member.result;

import com.example.demo.core.member.domain.Member;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FindMemberResult {

    private final long memberNo;

    private final String memberId;

    private final String memberName;

    private final LocalDateTime createdAt;

    public FindMemberResult(long memberNo, String memberId, String memberName, LocalDateTime createdAt) {
        this.memberNo = memberNo;
        this.memberId = memberId;
        this.memberName = memberName;
        this.createdAt = createdAt;
    }

    public static FindMemberResult from(Member member) {
        return new FindMemberResult(
            member.getMemberNo(),
            member.getMemberId(),
            member.getMemberName(),
            member.getCreatedAt()
        );
    }
}
