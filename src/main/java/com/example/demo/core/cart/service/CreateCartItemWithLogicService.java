package com.example.demo.core.cart.service;

import com.example.demo.core.cart.domain.CartItem;
import com.example.demo.core.cart.domain.NewCartItem;
import com.example.demo.core.cart.port.in.CartItemVO;
import com.example.demo.core.cart.port.in.CreateCartCommand;
import com.example.demo.core.cart.port.in.CreateCartWithLogicUseCase;
import com.example.demo.core.cart.port.out.CreateCartItemPort;
import com.example.demo.core.cart.port.out.FindCartItemPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateCartItemWithLogicService implements CreateCartWithLogicUseCase {

    private final FindCartItemPort findCartItemPort;
    private final CreateCartItemPort createCartItemPort;

    @Override
    public void create(final CreateCartCommand command) {
        doSomethingA();
        doSomethingB();

        final List<CartItem> cartItems = findCartItemPort.findAll(command.userId());

        createOrUpdate(command.cartItemVOs(), cartItems, command.userId());
    }

    private void createOrUpdate(final List<CartItemVO> cartItemVOs, final List<CartItem> cartItems, final Long userId) {
        cartItemVOs.forEach(cartItemVO -> {
            final Optional<CartItem> cartItem = cartItems.stream()
                .filter(it -> it.getProductId().equals(cartItemVO.productId()))
                .findFirst();

            if (cartItem.isPresent()) {
                cartItem.get().increaseQuantity(cartItemVO.quantity());
                createCartItemPort.create(cartItem.get());
            } else {
                createCartItemPort.create(CartItem.newCart(cartItemVO, userId));
            }
        });
    }

    private void doSomethingA() {}
    private void doSomethingB() {}
}
