package com.example.demo.infrastructure.persistence.product;

import com.example.demo.TestDataInsertSupport;
import com.example.demo.annotation.RepositoryTest;
import com.example.demo.common.enums.product.ProductStatus;
import com.example.demo.core.product.domain.Product;
import com.example.demo.core.product.domain.Stock;
import com.example.demo.core.product.param.SearchProductParam;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;

import static com.example.demo.ProductFixtures.PRODUCT_CODE;
import static com.example.demo.ProductFixtures.PRODUCT_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RepositoryTest
@DisplayName("ProductCustomRepositoryImpl")
class ProductCustomRepositoryImplTest extends TestDataInsertSupport {

    private ProductCustomRepositoryImpl productCustomRepositoryImpl;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @BeforeEach
    void init() {
        productCustomRepositoryImpl = new ProductCustomRepositoryImpl(jpaQueryFactory);
    }

    @Nested
    @DisplayName("search 메소드는")
    class Describe_search {
        final String productName = PRODUCT_NAME;
        final long minProductAmount = 5000;
        final long maxProductAmount = 10_000;

        @BeforeEach
        void setUp() {
            final List<Stock> stocks = List.of(
                new Stock("A202307300134", 10_000),
                new Stock("A202307300135", 1000),
                new Stock("A202307300136", 100),
                new Stock("A202307300137", 10)
            );
            saveAll(stocks);

            final List<Product> products = List.of(
                new Product("A202307300134", PRODUCT_NAME, ProductStatus.SELLING, maxProductAmount, stocks.get(0)),
                new Product("A202307300135", "깐풍기", ProductStatus.READY, minProductAmount, stocks.get(1)),
                new Product("A202307300136", "선풍기", ProductStatus.SELLING, 3000, stocks.get(2)),
                new Product("A202307300137", "냉장고", ProductStatus.SELLING, 50_000, stocks.get(3))
            );
            saveAll(products);
        }

        @Nested
        @DisplayName("검색 조건이 없으면,")
        class Context_notFoundData {

            final SearchProductParam param = new SearchProductParam(null, null, null, null, 0, 10);

            @Test
            @DisplayName("모든 데이터 4개를 리턴한다.")
            void it() {
                Page<Product> result = productCustomRepositoryImpl.search(param);

                assertEquals(4, result.getContent().size());
            }
        }

        @Nested
        @DisplayName("상품명 조건에 맞는 데이터가 존재한다면")
        class Context_foundProductNameData{

            final SearchProductParam param = new SearchProductParam("기", null, null, null, 0, 10);

            @Test
            @DisplayName("'기'가 포함된 데이터 2개를 리턴한다.")
            void it() {
                Page<Product> result = productCustomRepositoryImpl.search(param);

                assertEquals(2, result.getContent().size());
                result.getContent().forEach(product -> assertTrue(product.getProductName().contains("기")));
            }
        }

        @Nested
        @DisplayName("최소 금액 조건에 맞는 데이터가 존재한다면")
        class Context_foundMinProductAmountData{

            final SearchProductParam param = new SearchProductParam(null, minProductAmount, null, null, 0, 10);

            @Test
            @DisplayName("5000원보다 큰 데이터 3개를 리턴한다.")
            void it() {
                Page<Product> result = productCustomRepositoryImpl.search(param);

                assertEquals(3, result.getContent().size());
                result.getContent().forEach(product -> assertTrue(product.getProductAmount() >= 5000));
            }
        }

        @Nested
        @DisplayName("최대 금액 조건에 맞는 데이터가 존재한다면")
        class Context_foundMaxProductAmountData{

            final SearchProductParam param = new SearchProductParam(null, null, maxProductAmount, null, 0, 10);

            @Test
            @DisplayName("10_000보다 작은 데이터 3개를 리턴한다.")
            void it() {
                Page<Product> result = productCustomRepositoryImpl.search(param);

                assertEquals(3, result.getContent().size());
                result.getContent().forEach(product -> assertTrue(product.getProductAmount() <= 10_000));
            }
        }

        @Nested
        @DisplayName("상품 상태 조건에 맞는 데이터가 존재한다면")
        class Context_foundProductStatusData{

            final SearchProductParam param = new SearchProductParam(null, null, null, ProductStatus.READY, 0, 10);

            @Test
            @DisplayName("상품 준비중인 데이터 1개를 리턴한다.")
            void it() {
                Page<Product> result = productCustomRepositoryImpl.search(param);

                assertEquals(1, result.getContent().size());
                result.getContent().forEach(product -> assertEquals(ProductStatus.READY, product.getProductStatus()));
            }
        }
    }
}
