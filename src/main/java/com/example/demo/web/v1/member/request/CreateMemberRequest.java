package com.example.demo.web.v1.member.request;

import com.example.demo.core.member.param.CreateMemberParam;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;


public record CreateMemberRequest(@NotEmpty String memberId,
                                  @NotEmpty String memberName,
                                  @Email String email) {

    public CreateMemberParam toParam() {
        return new CreateMemberParam(memberId, memberName, email);
    }
}
