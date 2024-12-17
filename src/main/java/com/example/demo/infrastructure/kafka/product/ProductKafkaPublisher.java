package com.example.demo.infrastructure.kafka.product;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductKafkaPublisher {

    @Value("${kafka.producer.topics.create-product}")
    private String createProductTopic;

    private final KafkaTemplate<String, Message> kafkaTemplate;

    public void create() {
        kafkaTemplate.send(createProductTopic, "say hello~");
    }
}
