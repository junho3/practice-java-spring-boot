package com.example.demo.core.cart.service;

import com.example.demo.core.cart.domain.CartItem;
import com.example.demo.core.cart.port.in.CartItemVO;
import com.example.demo.core.cart.port.in.CreateCartCommand;
import com.example.demo.core.cart.port.in.CreateCartUseCase;
import com.example.demo.core.cart.port.out.CreateCartItemPort;
import com.example.demo.core.cart.port.out.FindCartItemPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateCartItemService implements CreateCartUseCase {

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
                .filter(it -> it.productId().equals(cartItemVO.productId()))
                .findFirst();

            if (cartItem.isPresent()) {
                createCartItemPort.create(new CartItem(
                    cartItem.get().cartItemId(),
                    cartItem.get().userId(),
                    cartItem.get().productId(),
                    cartItem.get().quantity() + cartItemVO.quantity(),
                    cartItem.get().codeA(),
                    cartItem.get().codeB(),
                    cartItem.get().createdAt()
                ));
            } else {
                createCartItemPort.create(new CartItem(
                    null,
                    userId,
                    cartItemVO.productId(),
                    cartItemVO.quantity(),
                    cartItemVO.codeA(),
                    null,
                    null
                ));
            }
        });
    }

    private void doSomethingA() {}
    private void doSomethingB() {}
}
