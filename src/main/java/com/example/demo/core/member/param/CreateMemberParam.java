package com.example.demo.core.member.param;

import jakarta.validation.constraints.NotNull;

public record CreateMemberParam(@NotNull String memberId,
                                @NotNull String memberName,
                                @NotNull String email) {
}
