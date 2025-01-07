package com.example.demo.core.product.service;

import com.example.demo.TestDataInsertSupport;
import com.example.demo.TestFixtures;
import com.example.demo.annotation.IntegrationTest;
import com.example.demo.core.product.domain.ProductSales;
import com.example.demo.core.product.param.FindProductSalesTop10Param;
import com.example.demo.core.product.result.FindProductSalesResult;
import com.example.demo.infrastructure.persistence.product.ProductSalesRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
@DisplayName("FindProductSalesService")
class FindProductSalesServiceTest extends TestDataInsertSupport {

    @Autowired
    private ProductSalesRepository productSalesRepository;

    @Autowired
    private FindProductSalesService findProductSalesService;

    @AfterEach
    void tearDown() {
        productSalesRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        final List<ProductSales> productSales = TestFixtures.get()
            .giveMeBuilder(ProductSales.class)
            .setNull("id")
            .setNotNull("productCode")
            .setNotNull("productName")
            .set("salesDate", LocalDate.of(2025, 1, 6))
            .setLazy("salesQuantity", List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L).iterator()::next)
            .sampleList(11);

        saveAll(productSales);
    }

    @Test
    @DisplayName("top10()은 판매날짜 조건으로 판매량이 가장 높은 상품 10개를 리턴한다.")
    void top10_when_given_salesDate_then_return_highest_saleQuantity_product() {
        final FindProductSalesTop10Param param = new FindProductSalesTop10Param(LocalDate.of(2025, 1, 6));

        final List<FindProductSalesResult> actual = findProductSalesService.top10(param);

        assertEquals(10, actual.size());
        assertEquals(11, actual.getFirst().salesQuantity());
    }
}
