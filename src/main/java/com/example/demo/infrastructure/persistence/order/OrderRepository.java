package com.example.demo.infrastructure.persistence.order;

import com.example.demo.core.order.domain.Order;
import com.example.demo.core.order.domain.OrderNo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNo(OrderNo orderNo);
}
