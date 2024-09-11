package com.example.demo.core.cart.port.in;

import jakarta.validation.constraints.NotNull;

public record CartItemVO(@NotNull Long productId,
                         @NotNull Long quantity,
                         @NotNull String codeA,
                         String codeB) {
}
