package com.example.demo;

import com.example.demo.core.product.domain.ProductSales;
import com.navercorp.fixturemonkey.customizer.InnerSpec;
import net.jqwik.api.Arbitraries;

import java.time.LocalDate;
import java.util.List;

public class ProductFixtures {
    private ProductFixtures() {}

    public static final String PRODUCT_CODE = "A000001";
    public static final String PRODUCT_NAME = "카스타드";

    public static final InnerSpec DEFAULT_PRODUCT_SALES_SPEC = new InnerSpec()
            .property("id", null)
            .property("productCode", Arbitraries.strings().alpha().ofMinLength(5).ofMaxLength(10))
            .property("productName", Arbitraries.strings().alpha().ofMinLength(2).ofMaxLength(10))
            .property("salesDate", LocalDate.of(2025, 1, 6))
            .property("salesQuantity", Arbitraries.longs().between(1L, 10_000L));

    public static ProductSales generateProductSales() {
        return TestFixtures.get()
                .giveMeBuilder(ProductSales.class)
                .setInner(DEFAULT_PRODUCT_SALES_SPEC)
                .sample();
    }

    public static ProductSales generateProductSales(final String productCode) {
        return TestFixtures.get()
                .giveMeBuilder(ProductSales.class)
                .setInner(DEFAULT_PRODUCT_SALES_SPEC)
                .set("productCode", productCode)
                .sample();
    }

    public static List<ProductSales> generateProductSalesList(final int size) {
        return TestFixtures.get()
                .giveMeBuilder(ProductSales.class)
                .setInner(DEFAULT_PRODUCT_SALES_SPEC)
                .sampleList(size);
    }
}
