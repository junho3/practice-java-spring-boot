package com.example.demo.infrastructure.persistence.member;

import lombok.Getter;

import java.util.Objects;

@Getter
public class MemberNickName2 {
    private final String value;

    public MemberNickName2(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberNickName2 that = (MemberNickName2) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
