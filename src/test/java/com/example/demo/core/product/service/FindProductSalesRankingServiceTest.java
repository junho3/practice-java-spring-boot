package com.example.demo.core.product.service;

import com.example.demo.TestDataInsertSupport;
import com.example.demo.TestFixtures;
import com.example.demo.annotation.IntegrationTest;
import com.example.demo.core.product.domain.ProductSalesRanking;
import com.example.demo.core.product.result.FindProductSalesRankingResult;
import com.example.demo.infrastructure.persistence.product.ProductSalesRankingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

@IntegrationTest
@DisplayName("FindProductSalesRankingService")
class FindProductSalesRankingServiceTest extends TestDataInsertSupport {

    @Autowired
    private ProductSalesRankingRepository productSalesRankingRepository;

    @Autowired
    private FindProductSalesRankingService findProductSalesRankingService;

    @AfterEach
    void tearDown() {
        productSalesRankingRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        final List<ProductSalesRanking> productSalesRankings = TestFixtures.get()
            .giveMeBuilder(ProductSalesRanking.class)
            .setNull("id")
            .setNotNull("productCode")
            .setNotNull("productName")
            .set("salesDate", LocalDate.of(2025, 1, 6))
            .setLazy("salesQuantity", List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L).iterator()::next)
            .sampleList(11);

        saveAll(productSalesRankings);
    }

    @Test
    @DisplayName("top10()은 판매날짜 조건으로 판매량이 가장 높은 상품 10개를 리턴한다.")
    void top10_when_given_salesDate_then_return_highest_saleQuantity_product() {
        final List<FindProductSalesRankingResult> actual = findProductSalesRankingService
            .top10(LocalDate.of(2025, 1, 6));

        Assertions.assertEquals(10, actual.size());
    }
}
