package com.example.demo.common.utils;

import com.example.demo.annotation.UnitTest;
import com.example.demo.core.order.domain.OrderNo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@UnitTest
@DisplayName("OrderNoGenerator")
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
                final OrderNo actual = orderNoGenerator.generate();

                assertThat(actual).isNotNull();
                assertThat(actual.getValue()).hasSize(orderNoLength);
            }
        }
    }
}
