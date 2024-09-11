package com.example.demo.core.cart.domain;

import com.example.demo.core.cart.port.in.CartItemVO;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CartItem {

    private Long cartItemId;
    @NotNull
    private Long userId;
    @NotNull
    private Long productId;
    @NotNull
    private Long quantity;
    @NotNull
    private String codeA;
    private String codeB;
    private LocalDateTime createdAt;

    public void increaseQuantity(final Long quantity) {
        this.quantity = this.quantity + quantity;
    }

    public static CartItem newCart(final CartItemVO cartItemVO, final Long userId) {
        return CartItem.builder()
            .userId(userId)
            .productId(cartItemVO.productId())
            .quantity(cartItemVO.quantity())
            .codeA(cartItemVO.codeA())
            .build();
    }
}
