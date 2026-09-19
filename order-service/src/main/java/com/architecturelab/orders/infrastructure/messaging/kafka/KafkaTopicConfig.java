package com.architecturelab.orders.infrastructure.messaging.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic orderCreatedTopic() {
        return new NewTopic(
                "orders.created",
                3,
                (short) 1
        );
    }
}
