package com.example.demo.infrastructure.persistence.product;

import com.example.demo.core.product.domain.ProductSalesRanking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProductSalesRankingRepository extends JpaRepository<ProductSalesRanking, Long> {
    List<ProductSalesRanking> findTop10BySalesDateOrderBySalesQuantityDesc(LocalDate salesDate);
}
