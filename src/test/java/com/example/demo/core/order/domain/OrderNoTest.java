package com.example.demo.core.order.domain;

import com.example.demo.annotation.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@UnitTest
@DisplayName("OrderNo")
class OrderNoTest {

    @ParameterizedTest
    @ValueSource(strings = { " ", "   ", "\t", "\n" })
    @NullAndEmptySource
    @DisplayName("주문번호는 NULL 또는 공백일 때 IllegalArgumentException()을 던진다.")
    void test1(String value) {
        assertThatThrownBy(() -> new OrderNo(value))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
