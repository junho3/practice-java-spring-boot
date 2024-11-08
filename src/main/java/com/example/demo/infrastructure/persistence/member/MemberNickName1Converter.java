package com.example.demo.infrastructure.persistence.member;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MemberNickName1Converter implements AttributeConverter<MemberNickName1, String> {
    @Override
    public String convertToDatabaseColumn(MemberNickName1 memberNickName) {
        return memberNickName == null ? null : memberNickName.getValue().toLowerCase();
    }

    @Override
    public MemberNickName1 convertToEntityAttribute(String s) {
        return s == null ? null : new MemberNickName1(s);
    }
}
