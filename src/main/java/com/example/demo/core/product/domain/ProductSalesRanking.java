package com.example.demo.core.product.domain;

import com.example.demo.config.persistence.AuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "product_sales_ranking")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductSalesRanking extends AuditEntity {
    @Id
    @Column(name = "product_sales_ranking_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_code", nullable = false, updatable = false)
    private String productCode;

    @Column(name = "product_name", nullable = false, updatable = false)
    private String productName;

    @Column(name = "sales_date", nullable = false, updatable = false)
    private LocalDate salesDate;

    @Column(name = "sales_quantity", nullable = false)
    private long salesQuantity;

    public ProductSalesRanking(final String productCode,
                               final String productName,
                               final LocalDate salesDate,
                               final long salesQuantity) {
        this.productCode = productCode;
        this.productName = productName;
        this.salesDate = salesDate;
        this.salesQuantity = salesQuantity;
    }
}
