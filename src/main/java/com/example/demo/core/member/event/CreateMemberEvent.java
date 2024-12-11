package com.example.demo.core.member.event;

import com.example.demo.core.member.domain.Member;

public record CreateMemberEvent(String memberId,
                                String memberName,
                                String email) {

    public static CreateMemberEvent from(final Member member) {
        return new CreateMemberEvent(member.getMemberId(),
            member.getMemberName(),
            member.getEmail());
    }
}
