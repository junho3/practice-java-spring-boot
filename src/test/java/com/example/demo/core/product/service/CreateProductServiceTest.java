package com.example.demo.core.product.service;

import com.example.demo.annotation.IntegrationTest;
import com.example.demo.common.exceptions.BusinessException;
import com.example.demo.core.product.domain.Product;
import com.example.demo.core.product.param.CreateProductParam;
import com.example.demo.core.stock.domain.Stock;
import com.example.demo.infrastructure.kafka.product.ProductKafkaPublisher;
import com.example.demo.infrastructure.persistence.product.ProductRepository;
import com.example.demo.infrastructure.persistence.stock.StockRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.test.context.EmbeddedKafka;

import static com.example.demo.ProductFixtures.PRODUCT_CODE;
import static com.example.demo.ProductFixtures.PRODUCT_NAME;
import static com.example.demo.common.exceptions.BusinessErrorCode.DUPLICATED_PRODUCT_CODE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("CreateProductService")
@EmbeddedKafka(
    brokerProperties = {
        "listeners=PLAINTEXT://localhost:0"
    }
)
@IntegrationTest
@RequiredArgsConstructor
class CreateProductServiceTest {
    private final CreateProductService createProductService;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final ProductKafkaPublisher productKafkaPublisher;

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
        stockRepository.deleteAll();
    }

    @Nested
    @DisplayName("create 메소드는")
    class Describe_create {

        private final String productCode = PRODUCT_CODE;
        private final String productName = PRODUCT_NAME;
        private final long quantity = 1_000;

        final CreateProductParam param = new CreateProductParam(
            productCode,
            productName,
            1_000,
            quantity,
            0
        );

        @AfterEach
        void tearDown() {
            productRepository.deleteAll();
            stockRepository.deleteAll();
        }

        @Nested
        @DisplayName("중복된 상품번호가 존재하지 않는다면")
        class Context_createNewProduct {

            @Test
            @DisplayName("Product와 Stock을 생성한다.")
            void it() {
                createProductService.create(param);

                Product product = productRepository.findByProductCode(productCode)
                    .orElseThrow();
                assertThat(product.getProductName()).isEqualTo(productName);

                Stock stock = stockRepository.findByProductCode(productCode)
                        .orElseThrow();
                assertThat(stock.getQuantity()).isEqualTo(quantity);
            }
        }

        @Nested
        @DisplayName("중복된 상품번호가 존재한다면")
        class Context_duplicateProduct {

            @BeforeEach
            void setUp() {
                createProductService.create(param);
            }

            @Test
            @DisplayName("BusinessException을 던진다.")
            void it() {
                assertThatThrownBy(() -> createProductService.create(param))
                    .isExactlyInstanceOf(BusinessException.class)
                    .extracting("businessErrorCode").isEqualTo(DUPLICATED_PRODUCT_CODE);
            }
        }
    }
}
