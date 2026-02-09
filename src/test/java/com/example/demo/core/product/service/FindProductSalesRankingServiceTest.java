package com.example.demo.core.product.service;

import com.example.demo.ProductFixtures;
import com.example.demo.annotation.UnitTest;
import com.example.demo.core.product.domain.ProductSales;
import com.example.demo.core.product.param.FindProductSalesTop10Param;
import com.example.demo.core.product.result.FindProductSalesResult;
import com.example.demo.infrastructure.persistence.product.ProductSalesRepository;
import com.example.demo.infrastructure.persistence.product.ProductSalesRankingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@UnitTest
@DisplayName("FindProductSalesRankingService")
class FindProductSalesRankingServiceTest {

    @InjectMocks
    private FindProductSalesRankingService findProductSalesRankingService;

    @Mock
    private ProductSalesRankingRepository productSalesRankingRepository;

    @Mock
    private ProductSalesRepository productSalesRepository;

    private static final LocalDate SALES_DATE = LocalDate.of(2025, 1, 6);

    @Nested
    @DisplayName("top10 메소드는")
    class Describe_top10 {

        final FindProductSalesTop10Param param = new FindProductSalesTop10Param(SALES_DATE);

        @Nested
        @DisplayName("Redis에 랭킹 데이터가 존재할 때")
        class Context_ranking_exists {

            final List<String> productCodes = List.of("P003", "P001", "P002");

            @BeforeEach
            void setUp() {
                final List<ProductSales> productSalesList = List.of(
                        ProductFixtures.generateProductSales("P001"),
                        ProductFixtures.generateProductSales("P002"),
                        ProductFixtures.generateProductSales("P003")
                );

                given(productSalesRankingRepository.findTop10(SALES_DATE))
                        .willReturn(productCodes);
                given(productSalesRepository.findBySalesDateAndProductCodeIn(SALES_DATE, productCodes))
                        .willReturn(productSalesList);
            }

            @Test
            @DisplayName("Redis 랭킹 순서대로 상품 판매 정보를 리턴한다.")
            void it_returns_product_sales_by_ranking_order() {
                final List<FindProductSalesResult> actual = findProductSalesRankingService.top10(param);

                assertThat(actual).hasSize(3);
                assertThat(actual.get(0).productCode()).isEqualTo("P003");
                assertThat(actual.get(1).productCode()).isEqualTo("P001");
                assertThat(actual.get(2).productCode()).isEqualTo("P002");
            }
        }

        @Nested
        @DisplayName("Redis에 랭킹 데이터가 없을 때")
        class Context_ranking_not_exists {

            @BeforeEach
            void setUp() {
                given(productSalesRankingRepository.findTop10(SALES_DATE))
                        .willReturn(List.of());
            }

            @Test
            @DisplayName("IllegalStateException을 던진다.")
            void it_throws_illegal_state_exception() {
                assertThatThrownBy(() -> findProductSalesRankingService.top10(param))
                        .isExactlyInstanceOf(IllegalStateException.class);

                then(productSalesRepository).should(never())
                        .findBySalesDateAndProductCodeIn(SALES_DATE, List.of());
            }
        }
    }
}
