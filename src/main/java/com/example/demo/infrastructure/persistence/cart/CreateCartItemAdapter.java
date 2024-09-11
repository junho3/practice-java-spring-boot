package com.example.demo.infrastructure.persistence.cart;

import com.example.demo.core.cart.domain.CartItem;
import com.example.demo.core.cart.domain.NewCartItem;
import com.example.demo.core.cart.port.out.CreateCartItemPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class CreateCartItemAdapter implements CreateCartItemPort {

    private final CartItemRepository cartItemRepository;

    public CreateCartItemAdapter(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public void create(final List<CartItem> cartItems) {
        cartItemRepository.saveAll(cartItems.stream()
            .map(cartItem -> new CartItemEntity(
                cartItem.cartItemId(),
                cartItem.userId(),
                cartItem.productId(),
                cartItem.quantity(),
                cartItem.codeA(),
                cartItem.codeB()
                )).toList());
    }

    @Override
    public void createOrUpdate(final List<NewCartItem> newCartItems, final Long userId) {
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
