package com.example.demo.core.member.param;

public record SendMemberEmailParam(String memberId,
                                   String memberName,
                                   String email) {}
