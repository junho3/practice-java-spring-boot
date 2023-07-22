package com.example.demo.core.member.param;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateMemberParam {

    private String memberId;

    private String memberName;
}
