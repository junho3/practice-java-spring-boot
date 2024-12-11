package com.example.demo.core.member.param;

import com.example.demo.core.member.event.CreateMemberEvent;

public record SendMemberEmailParam(String memberId,
                                   String memberName,
                                   String email) {

    public static SendMemberEmailParam from(final CreateMemberEvent event) {
        return new SendMemberEmailParam(event.memberId(), event.memberName(), event.email());
    }
}
