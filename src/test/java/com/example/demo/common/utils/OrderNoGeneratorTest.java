package com.example.demo.common.utils;

import com.example.demo.annotation.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.demo.MemberFixtures.MEMBER_ID;
import static com.example.demo.MemberFixtures.MEMBER_NAME;
import static org.junit.jupiter.api.Assertions.*;

@UnitTest
class OrderNoGeneratorTest {

    private final OrderNoGenerator orderNoGenerator = new OrderNoGenerator();

    @Nested
    @DisplayName("generate 메소드는")
    class Describe_generate {

        final int orderNoLength = 10;

        @Nested
        @DisplayName("호출되면")
        class Context_call {

            @Test
            @DisplayName("랜덤 10자리 문자열을 리턴한다.")
            void it() {
                String orderNo = orderNoGenerator.generate();

                assertNotNull(orderNo);
                assertEquals(orderNoLength, orderNo.length());
            }
        }
    }
}
