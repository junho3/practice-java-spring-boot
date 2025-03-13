package com.example.demo.core.order.service;

import com.example.demo.config.persistence.ReadTransactional;
import com.example.demo.core.order.domain.Order;
import com.example.demo.core.order.domain.OrderNo;
import com.example.demo.core.order.result.OrderAggregateResult;
import com.example.demo.infrastructure.persistence.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@ReadTransactional
@RequiredArgsConstructor
public class FindOrderService {

    private final OrderRepository orderRepository;

    public OrderAggregateResult findAggregate(final OrderNo orderNo) {
        final Order order = orderRepository.findByOrderNo(orderNo)
            .orElseThrow(() -> new NoSuchElementException("주문 데이터가 없습니다."));

        return OrderAggregateResult.from(order);
    }
}
