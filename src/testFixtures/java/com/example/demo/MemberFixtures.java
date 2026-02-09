package com.example.demo;

import com.example.demo.core.member.param.CreateMemberParam;
import com.navercorp.fixturemonkey.customizer.InnerSpec;
import net.jqwik.api.Arbitraries;

public final class MemberFixtures {
    private MemberFixtures() {}

    public static final long MEMBER_NO = 723;
    public static final String MEMBER_ID = "banana";
    public static final String MEMBER_NAME = "춘식이";
    public static final String EMAIL = "seoul@korea.com";

    public static final InnerSpec DEFAULT_CREATE_MEMBER_PARAM_SPEC = new InnerSpec()
            .property("memberId", Arbitraries.strings().alpha().ofMinLength(5).ofMaxLength(20))
            .property("memberName", Arbitraries.strings().alpha().ofMinLength(2).ofMaxLength(30))
            .property("email", Arbitraries.strings().alpha().ofMinLength(5).ofMaxLength(20));

    public static CreateMemberParam generateCreateMemberParam() {
        return TestFixtures.get()
                .giveMeBuilder(CreateMemberParam.class)
                .setInner(DEFAULT_CREATE_MEMBER_PARAM_SPEC)
                .sample();
    }
}
