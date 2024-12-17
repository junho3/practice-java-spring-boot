package com.example.demo.infrastructure.kafka.product;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductKafkaPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void create() {
        kafkaTemplate.send("topic", "say hello~");
    }
}
