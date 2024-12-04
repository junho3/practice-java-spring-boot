package com.example.demo.core.member.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MemberNickName2Converter implements AttributeConverter<MemberNickName2, String> {
    @Override
    public String convertToDatabaseColumn(MemberNickName2 memberNickName) {
        return memberNickName == null ? null : memberNickName.getValue().toLowerCase();
    }

    @Override
    public MemberNickName2 convertToEntityAttribute(String s) {
        return s == null ? null : new MemberNickName2(s);
    }
}
