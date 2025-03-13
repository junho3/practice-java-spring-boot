package com.example.demo.core.order.service;

import com.example.demo.OrderFixtures;
import com.example.demo.annotation.RepositoryTest;
import com.example.demo.core.order.domain.OrderNo;
import com.example.demo.core.order.result.OrderAggregateResult;
import com.example.demo.infrastructure.persistence.order.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RepositoryTest
@DisplayName("FindOrderService")
class FindOrderServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    private FindOrderService sut;

    @BeforeEach
    void setup() {
        sut = new FindOrderService(orderRepository);
    }

    @Test
    @DisplayName("findAggregate()는 주문이 존재하지 않을 때 NoSuchElementException()을 던진다.")
    void test1() {
        final OrderNo orderNo = OrderFixtures.generateOrderNo();

        assertThatThrownBy(() -> sut.findAggregate(orderNo))
            .isExactlyInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("findAggregate()는 주문이 존재할 때 Aggregate 객체를 리턴한다.")
    void test2() {
        final OrderNo orderNo = OrderFixtures.generateOrderNo();

        orderRepository.saveAndFlush(OrderFixtures.generateOrderAggregate(orderNo));

        final OrderAggregateResult actual = sut.findAggregate(orderNo);

        assertThat(actual.orderNo()).isEqualTo(orderNo);
        assertThat(actual.products()).isNotEmpty();
    }
}
