package com.example.demo;

import com.example.demo.core.order.domain.Order;
import com.example.demo.core.order.domain.OrderNo;
import com.example.demo.core.order.domain.OrderProduct;
import com.navercorp.fixturemonkey.customizer.InnerSpec;
import net.jqwik.api.Arbitraries;

import java.math.BigDecimal;
import java.util.List;

public class OrderFixtures {
    private OrderFixtures() {}

    public static final InnerSpec DEFAULT_ORDER_PRODUCT_SPEC = new InnerSpec()
        .property("orderProductId", null)
        .property("productCode", Arbitraries.strings().alpha())
        .property("productName", Arbitraries.strings().alpha())
        .property("quantity", Arbitraries.integers().between(1, 100))
        .property("productAmount", Arbitraries.bigDecimals().between(BigDecimal.ONE, BigDecimal.valueOf(1_000_000)));
    public static final InnerSpec DEFAULT_ORDER_INNER_SPEC = new InnerSpec()
        .property("orderId", null)
        .property("memberNo", Arbitraries.longs().greaterOrEqual(1L))
        .property("orderName", Arbitraries.strings().alpha())
        .property("transactionAmount", Arbitraries.bigDecimals().between(BigDecimal.ONE, BigDecimal.valueOf(1_000_000)))
        .property("products", products -> products.size(0));

    public static OrderNo generateOrderNo() {
        return TestFixtures.get()
            .giveMeBuilder(OrderNo.class)
            .set("value", Arbitraries.strings().numeric())
            .sample();
    }

    public static Order generateOrderAggregate(final OrderNo orderNo) {
        final Order order = TestFixtures.get()
            .giveMeBuilder(Order.class)
            .set("orderNo", orderNo)
            .setInner(DEFAULT_ORDER_INNER_SPEC)
            .sample();

        final List<OrderProduct> products = TestFixtures.get()
            .giveMeBuilder(OrderProduct.class)
            .setInner(DEFAULT_ORDER_PRODUCT_SPEC)
            .set("order", order)
            .sampleList(3);

        return order
            .addProducts(products);
    }
}
