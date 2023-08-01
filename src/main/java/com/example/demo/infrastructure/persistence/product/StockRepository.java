package com.example.demo.infrastructure.persistence.product;

import com.example.demo.core.product.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByProductCode(String productCode);
}
