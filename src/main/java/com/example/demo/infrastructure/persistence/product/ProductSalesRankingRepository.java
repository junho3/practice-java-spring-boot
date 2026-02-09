package com.example.demo.infrastructure.persistence.product;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;

public interface ProductSalesRankingRepository {

    void save(String productCode, long salesQuantity, LocalDate salesDate);

    List<String> findTop10(LocalDate salesDate);

    LinkedHashMap<String, Long> findTop10WithScores(LocalDate salesDate);
}
