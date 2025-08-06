package com.example.demo.core.product.service;

import com.example.demo.TestDataInsertSupport;
import com.example.demo.annotation.IntegrationTest;
import com.example.demo.common.enums.product.ProductStatus;
import com.example.demo.core.product.domain.FoodProduct;
import com.example.demo.core.product.param.SearchProductParam;
import com.example.demo.core.product.result.SearchProductResult;
import com.example.demo.core.stock.domain.Stock;
import com.example.demo.infrastructure.persistence.product.ProductRepository;
import com.example.demo.infrastructure.persistence.stock.StockRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.example.demo.ProductFixtures.PRODUCT_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SearchProductService")
@IntegrationTest
@RequiredArgsConstructor
class SearchProductServiceTest extends TestDataInsertSupport {

    private final SearchProductService searchProductService;
    private final StockRepository stockRepository;
    private final ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
        stockRepository.deleteAll();
    }

    @Nested
    @DisplayName("search 메소드는")
    class Describe_search {
        final long minProductAmount = 5000;
        final long maxProductAmount = 10_000;

        @BeforeEach
        void setUp() {
            final Stock stock = new Stock("A202307300134", 10_000, 0);
            save(stock);
            save(new FoodProduct("A202307300134", PRODUCT_NAME, ProductStatus.SELLING, maxProductAmount, stock, LocalDate.now()));
        }

        @Nested
        @DisplayName("검색 조건에 맞는 데이터가 존재한다면")
        class Context_found_data {

            final SearchProductParam param = new SearchProductParam(null, null, null, null, 0, 10);

            @Test
            @DisplayName("데이터를 리턴한다.")
            void it() {
                final SearchProductResult actual = searchProductService.search(param);

                assertThat(actual).isExactlyInstanceOf(SearchProductResult.class);
                assertThat(actual.getProducts()).hasSize(1);
            }
        }
    }
}
