package com.example.demo.core.member.param;

import lombok.Getter;

@Getter
public class CreateMemberParam {

    private final String memberId;

    private final String memberName;

    public CreateMemberParam(String memberId, String memberName) {
        this.memberId = memberId;
        this.memberName = memberName;
    }
}
