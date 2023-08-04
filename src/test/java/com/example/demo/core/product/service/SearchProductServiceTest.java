package com.example.demo.core.product.service;

import com.example.demo.TestDataInsertSupport;
import com.example.demo.annotation.IntegrationTest;
import com.example.demo.common.enums.product.ProductStatus;
import com.example.demo.core.product.domain.Product;
import com.example.demo.core.product.domain.Stock;
import com.example.demo.core.product.param.SearchProductParam;
import com.example.demo.core.product.result.FindProductResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.demo.ProductFixtures.PRODUCT_NAME;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@IntegrationTest
@DisplayName("SearchProductService")
class SearchProductServiceTest extends TestDataInsertSupport {

    @Autowired
    private SearchProductService searchProductService;

    @Nested
    @DisplayName("search 메소드는")
    class Describe_search {
        final long minProductAmount = 5000;
        final long maxProductAmount = 10_000;

        @BeforeEach
        void setUp() {
            final Stock stock = new Stock("A202307300134", 10_000, 0);
            save(stock);
            save(new Product("A202307300134", PRODUCT_NAME, ProductStatus.SELLING, maxProductAmount, stock));
        }

        @Nested
        @DisplayName("검색 조건에 맞는 데이터를 조회하여")
        class Context_notFoundData {

            final SearchProductParam param = new SearchProductParam(null, null, null, null, 0, 10);

            @Test
            @DisplayName("FindProductResult 타입으로 리턴한다.")
            void it() {
                FindProductResult result = searchProductService.search(param);

                assertInstanceOf(FindProductResult.class, result);
            }
        }
    }
}
