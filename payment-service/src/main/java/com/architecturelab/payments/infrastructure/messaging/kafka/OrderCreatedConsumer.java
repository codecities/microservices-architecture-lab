package com.architecturelab.payments.infrastructure.messaging.kafka;

import com.architecturelab.payments.application.event.OrderCreatedEvent;
import com.architecturelab.payments.application.event.service.PaymentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component
public class OrderCreatedConsumer {

    private final ObjectMapper objectMapper;
    private final PaymentService paymentService;

    public OrderCreatedConsumer(
            ObjectMapper objectMapper,
            PaymentService paymentService
    ) {
        this.objectMapper = objectMapper;
        this.paymentService = paymentService;
    }

    @KafkaListener(
            topics = "orders.created",
            groupId = "payment-service"
    )
    public void consume(String payload) throws JacksonException {
        OrderCreatedEvent event =
                objectMapper.readValue(
                        payload,
                        OrderCreatedEvent.class
                );

        paymentService.createPayment(event);
    }
}
