package com.example.demo.core.order.service;

import com.example.demo.TestFixtures;
import com.example.demo.annotation.IntegrationTest;
import com.example.demo.common.utils.OrderNoGenerator;
import com.example.demo.core.order.domain.Order;
import com.example.demo.core.order.domain.OrderNo;
import com.example.demo.core.order.param.CreateOrderParam;
import com.example.demo.core.order.result.CreateOrderResult;
import com.example.demo.infrastructure.persistence.order.OrderRepository;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;

import static com.example.demo.OrderFixtures.ORDER_NO;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;

@IntegrationTest
@DisplayName("CreateOrderService")
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
        private final OrderNo orderNo = ORDER_NO;

        final CreateOrderParam param = TestFixtures.get()
            .giveMeBuilder(CreateOrderParam.class)
            .size("products", 1, 10)
            .set("products[*].quantity", Arbitraries.integers().between(1, 100))
            .set("products[*].productAmount", Arbitraries.bigDecimals().between(BigDecimal.ONE, BigDecimal.valueOf(100_000)))
            .sample();

        @BeforeEach
        void setUp() {
            given(orderNoGenerator.generate())
                .willReturn(orderNo);
        }

        @Nested
        @DisplayName("중복된 주문번호가 존재하지 않는다면")
        class Context_createNewOrderNo {

            @Test
            @DisplayName("Order를 생성한다.")
            void it() {
                final CreateOrderResult actual = createOrderService.create(param);

                assertThat(actual.orderNo()).isEqualTo(orderNo);
                assertThat(actual.products()).hasSize(param.products().size());
            }
        }

        @Nested
        @DisplayName("중복된 주문번호가 존재하면")
        class Context_createDuplicateOrderNo {

            @BeforeEach
            void setUp() {
                final Order order = TestFixtures.get()
                    .giveMeBuilder(Order.class)
                    .setNull("orderId")
                    .set("orderNo", orderNo)
                    .setNotNull("orderName")
                    .set("transactionAmount", BigDecimal.ONE)
                    .size("products", 0)
                    .sample();

                orderRepository.saveAndFlush(order);
            }

            @Test
            @DisplayName("DataIntegrityViolationException()을 던진다.")
            void it() {
                assertThatThrownBy(() -> createOrderService.create(param))
                    .isExactlyInstanceOf(DataIntegrityViolationException.class);
            }
        }
    }
}
