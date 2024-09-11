package com.example.demo.infrastructure.persistence.cart;

import com.example.demo.core.cart.domain.CartItem;
import com.example.demo.core.cart.domain.NewCartItem;
import com.example.demo.core.cart.port.out.CreateCartItemPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Transactional
@RequiredArgsConstructor
public class CreateCartItemAdapter implements CreateCartItemPort {

    private final CartItemRepository cartItemRepository;

    @Override
    public void create(final CartItem cartItem) {
        cartItemRepository.save(new CartItemEntity(
            cartItem.getCartItemId(),
            cartItem.getUserId(),
            cartItem.getProductId(),
            cartItem.getQuantity(),
            cartItem.getCodeA(),
            cartItem.getCodeB()));
    }

    @Override
    public void createOrUpdates(final List<NewCartItem> newCartItems, final Long userId) {
        final List<CartItemEntity> cartItemEntities = cartItemRepository.findAllByUserId(userId);

        newCartItems.forEach(newCartItem -> {
            final Optional<CartItemEntity> cartItemEntity = cartItemEntities.stream()
                .filter(entity -> entity.getProductId().equals(newCartItem.productId()))
                .findFirst();

            if (cartItemEntity.isPresent()) {
                // 데이터가 있으면 업데이트
                cartItemEntity.get().increaseQuantity(newCartItem.quantity());
            } else {
                // 데이터가 없으면 인서트
                cartItemRepository.save(new CartItemEntity(
                    newCartItem.userId(),
                    newCartItem.productId(),
                    newCartItem.quantity(),
                    newCartItem.codeA()));
            }
        });
    }
}
