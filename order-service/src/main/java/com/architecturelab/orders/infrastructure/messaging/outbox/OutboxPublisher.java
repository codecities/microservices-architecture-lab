package com.architecturelab.orders.infrastructure.messaging.outbox;

import com.architecturelab.orders.infrastructure.persistence.jpa.OutboxEventJpaEntity;
import com.architecturelab.orders.infrastructure.persistence.jpa.repository.SpringDataOutboxRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutboxPublisher {

    private final SpringDataOutboxRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxPublisher(
            SpringDataOutboxRepository repository,
            KafkaTemplate<String, String> kafkaTemplate
    ) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 1000)
    public void publishPendingEvents() {

        List<OutboxEventJpaEntity> events =
                repository.findTop100ByPublishedAtIsNullOrderByOccurredAtAsc();

        for (OutboxEventJpaEntity event: events) {

            kafkaTemplate
                    .send(
                            "orders.created",
                            event.getAggregateId().toString(),
                            event.getPayload()
                    )
                    .whenComplete((result, exception) -> {

                        if (exception == null) {
                            event.markAsPublished();
                            repository.save(event);
                        }
                    });
        }
    }
}
