package com.example.demo.core.order.service;

import com.example.demo.annotation.IntegrationTest;
import com.example.demo.common.utils.OrderNoGenerator;
import com.example.demo.core.order.domain.Order;
import com.example.demo.core.order.param.CreateOrderParam;
import com.example.demo.infrastructure.persistence.order.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static com.example.demo.MemberFixtures.MEMBER_NO;
import static com.example.demo.OrderFixtures.ORDER_NO;
import static com.example.demo.ProductFixtures.PRODUCT_CODE;
import static com.example.demo.ProductFixtures.PRODUCT_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@IntegrationTest
class CreateOrderServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @InjectMocks
    private CreateOrderService createOrderService;

    @Mock
    private OrderNoGenerator orderNoGenerator;

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
    }

    @BeforeEach()
    void setUp() {
        createOrderService = new CreateOrderService(orderRepository, orderNoGenerator);
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {
        @Nested
        @DisplayName("CreateOrderParam이 주어지면")
        class Context {

            private final long memberNo = MEMBER_NO;

            private final String orderNo = ORDER_NO;

            final CreateOrderParam param = new CreateOrderParam(
                memberNo,
                Set.of(
                    new CreateOrderParam.Product(PRODUCT_CODE, PRODUCT_NAME, 3, 2000),
                    new CreateOrderParam.Product("B100000", "수박", 1, 4000)
                )
            );

            @BeforeEach
            void setUp() {
                given(orderNoGenerator.generate())
                    .willReturn(orderNo);
            }

            @Test
            @DisplayName("Order를 생성한다.")
            void it() {
                createOrderService.create(param);

                Order order = orderRepository.findByOrderNo(orderNo).orElseThrow();

                assertEquals(orderNo, order.getOrderNo());
                assertEquals(10_000L, order.getTransactionAmount());
            }
        }
    }
}
