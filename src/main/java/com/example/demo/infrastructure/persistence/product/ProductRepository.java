package com.example.demo.infrastructure.persistence.product;

import com.example.demo.core.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    //
}
