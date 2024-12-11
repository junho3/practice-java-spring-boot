package com.example.demo.web.v1.member.request;

import com.example.demo.core.member.param.CreateMemberParam;

public record CreateMemberRequest(String memberId,
                                  String memberName,
                                  String email) {

    public CreateMemberParam toParam() {
        return new CreateMemberParam(memberId, memberName, email);
    }
}
