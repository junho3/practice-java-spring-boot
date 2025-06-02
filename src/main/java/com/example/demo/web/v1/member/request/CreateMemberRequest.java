package com.example.demo.web.v1.member.request;

import com.example.demo.core.member.param.CreateMemberParam;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateMemberRequest(
    @NotEmpty
    String memberId,
    @NotEmpty
    String memberName,
    @Email(message = "유효하지 않은 이메일 포맷입니다.")
    @NotNull
    String email
) {
    public CreateMemberParam toParam() {
        return new CreateMemberParam(memberId, memberName, email);
    }
}
