package com.example.demo.core.order.service;

import com.example.demo.common.utils.OrderNoGenerator;
import com.example.demo.core.order.param.CreateOrderParam;
import com.example.demo.infrastructure.persistence.order.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateOrderService {

    private final OrderRepository orderRepository;

    private final OrderNoGenerator orderNoGenerator;

    public CreateOrderService(OrderRepository orderRepository, OrderNoGenerator orderNoGenerator) {
        this.orderRepository = orderRepository;
        this.orderNoGenerator = orderNoGenerator;
    }

    @Transactional
    public void create(CreateOrderParam param) {
        String orderNo = orderNoGenerator.generate();

        orderRepository.save(param.toEntity(orderNo));
    }
}
