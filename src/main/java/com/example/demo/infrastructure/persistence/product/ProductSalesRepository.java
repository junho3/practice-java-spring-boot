package com.example.demo.infrastructure.persistence.product;

import com.example.demo.core.product.domain.ProductSales;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProductSalesRepository extends JpaRepository<ProductSales, Long> {
    List<ProductSales> findTop10BySalesDateOrderBySalesQuantityDesc(LocalDate salesDate);
}
