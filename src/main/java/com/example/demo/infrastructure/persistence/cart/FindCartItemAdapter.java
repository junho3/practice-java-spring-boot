package com.example.demo.infrastructure.persistence.cart;

import com.example.demo.core.cart.domain.CartItem;
import com.example.demo.core.cart.port.out.FindCartItemPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindCartItemAdapter implements FindCartItemPort {

    private final CartItemRepository cartItemRepository;

    @Override
    public List<CartItem> findAll(final Long userId) {
        return cartItemRepository.findAllByUserId(userId).stream()
            .map(entity -> CartItem.builder()
                .cartItemId(entity.getCartItemId())
                .userId(entity.getUserId())
                .productId(entity.getProductId())
                .quantity(entity.getQuantity())
                .codeA(entity.getCodeA())
                .codeB(entity.getCodeB())
                .createdAt(entity.getCreatedAt())
                .build())
            .toList();
    }
}
