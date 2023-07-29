package com.example.demo.infrastructure.persistence.product;

import com.example.demo.core.product.domain.Product;
import org.springframework.data.repository.Repository;

public interface ProductRepository extends Repository<Product, Long> {

    Product save(Product product);
}
