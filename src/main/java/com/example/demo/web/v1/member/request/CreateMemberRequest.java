package com.example.demo.web.v1.member.request;

import com.example.demo.core.member.param.CreateMemberParam;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateMemberRequest {

    @NotEmpty
    private String memberId;

    @NotEmpty
    private String memberName;

    public CreateMemberParam toParam() {
        return CreateMemberParam.builder()
            .memberId(memberId)
            .memberName(memberName)
            .build();
    }
}
