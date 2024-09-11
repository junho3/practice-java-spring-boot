package com.example.demo.core.cart.domain;

import com.example.demo.core.cart.port.in.CartItemVO;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CartItem")
class CartItemTest {

    final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
        .plugin(new JakartaValidationPlugin())
        .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
        .build();

    @Test
    @DisplayName("newCart()는 저장 가능한 카트 객체를 리턴한다.")
    void newCart_return_creatable_cart_object() {
        final CartItemVO cartItemVO = fixtureMonkey.giveMeOne(CartItemVO.class);
        final Long userId = 1L;

        final CartItem sut = CartItem.newCart(cartItemVO, userId);

        assertThat(sut.getCartItemId()).isNull();
        assertThat(sut.getUserId()).isEqualTo(userId);
        assertThat(sut.getProductId()).isEqualTo(cartItemVO.productId());
        assertThat(sut.getQuantity()).isEqualTo(cartItemVO.quantity());
        assertThat(sut.getCodeA()).isEqualTo(cartItemVO.codeA());
        assertThat(sut.getCodeB()).isNull();
        assertThat(sut.getCreatedAt()).isNull();
    }
}
