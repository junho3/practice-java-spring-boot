package com.example.demo;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class TestTransactionEventSupport {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final EntityManager entityManager;

    public void publish(final Object event) {
        entityManager.persist(new DummyEntity());
        applicationEventPublisher.publishEvent(event);
        entityManager.flush();
        entityManager.clear();
    }
}
