package com.example.demo.core.cart.service;

import com.example.demo.core.cart.domain.NewCartItem;
import com.example.demo.core.cart.port.in.CreateCartCommand;
import com.example.demo.core.cart.port.in.CreateCartUseCase;
import com.example.demo.core.cart.port.out.CreateCartItemPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCartItemService implements CreateCartUseCase {

    private final CreateCartItemPort createCartItemPort;

    @Override
    public void create(final CreateCartCommand command) {
        doSomethingA();
        doSomethingB();

        createCartItemPort.createOrUpdates(command.cartItemVOs().stream()
                .map(cartItemVO -> new NewCartItem(
                    command.userId(),
                    cartItemVO.productId(),
                    cartItemVO.quantity(),
                    cartItemVO.codeA()
                )).toList(),
            command.userId());
    }

    private void doSomethingA() {}
    private void doSomethingB() {}
}
