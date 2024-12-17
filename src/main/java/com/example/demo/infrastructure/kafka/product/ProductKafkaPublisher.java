package com.example.demo.infrastructure.kafka.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.kafka.support.KafkaHeaders.KEY;
import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductKafkaPublisher {

    @Value("${kafka.producer.topics.create-product}")
    private String createProductTopic;

    private final KafkaTemplate<String, Message> kafkaTemplate;

    public void create(final CreateProductPayload payload) {
        kafkaTemplate.send(MessageBuilder.withPayload(payload)
                .setHeader(TOPIC, createProductTopic)
                .setHeader(KEY, payload.productCode())
            .build())
            .thenAccept(result -> log.info("카프카 메시지 발행 성공!!"))
            .exceptionally(exception -> {
                log.error("메시지 발행 실패");
                return null;
            });
    }
}
