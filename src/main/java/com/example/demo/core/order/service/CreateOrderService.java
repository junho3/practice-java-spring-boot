package com.example.demo.core.order.service;

import com.example.demo.common.utils.OrderNoGenerator;
import com.example.demo.core.order.domain.Order;
import com.example.demo.core.order.domain.OrderNo;
import com.example.demo.core.order.domain.OrderProduct;
import com.example.demo.core.order.param.CreateOrderParam;
import com.example.demo.core.order.result.CreateOrderResult;
import com.example.demo.infrastructure.persistence.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateOrderService {

    private final OrderRepository orderRepository;
    private final OrderNoGenerator orderNoGenerator;

    @Transactional
    public CreateOrderResult create(final CreateOrderParam param) {
        final OrderNo orderNo = orderNoGenerator.generate();

        final Order order = new Order(
            orderNo,
            param.memberNo(),
            param.getOrderName(),
            param.getTransactionAmount());

        final List<OrderProduct> orderProducts = param.products().stream()
            .map(it -> new OrderProduct(it.productCode(), it.productName(), it.quantity(), it.productAmount()))
            .toList();

        order.addProducts(orderProducts);

        orderRepository.save(order);

        return CreateOrderResult.from(order);
    }
}
