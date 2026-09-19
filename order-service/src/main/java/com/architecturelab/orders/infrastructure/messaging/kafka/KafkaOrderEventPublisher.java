package com.architecturelab.orders.infrastructure.messaging.kafka;

import com.architecturelab.orders.application.event.OrderCreatedEvent;
import com.architecturelab.orders.application.port.OrderEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaOrderEventPublisher implements OrderEventPublisher {

    private static final String TOPIC = "orders.created";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaOrderEventPublisher(
            KafkaTemplate<String, Object> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(OrderCreatedEvent event) {
        kafkaTemplate.send(
                TOPIC,
                event.orderId().toString(),
                event
        );
    }
}
