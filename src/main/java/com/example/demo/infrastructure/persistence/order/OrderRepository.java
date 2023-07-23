package com.example.demo.infrastructure.persistence.order;

import com.example.demo.core.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
