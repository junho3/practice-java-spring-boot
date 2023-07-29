package com.example.demo.infrastructure.persistence.product;

import com.example.demo.core.product.domain.Stock;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface StockRepository extends Repository<Stock, Long> {

    Stock save(Stock stock);
}
