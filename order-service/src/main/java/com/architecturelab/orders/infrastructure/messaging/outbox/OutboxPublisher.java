package com.architecturelab.orders.infrastructure.messaging.outbox;

import com.architecturelab.orders.infrastructure.persistence.jpa.OutboxEventJpaEntity;
import com.architecturelab.orders.infrastructure.persistence.jpa.repository.SpringDataOutboxRepository;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
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

            ProducerRecord<String, String> record =
                    new ProducerRecord<>(
                            "orders.created",
                            event.getAggregateId().toString(),
                            event.getPayload()
                    );

            if (event.getTraceparent() != null) {
                record.headers().add(
                        new RecordHeader(
                                "traceparent",
                                event.getTraceparent()
                                        .getBytes(StandardCharsets.UTF_8)
                        )
                );
            }

            if (event.getTracestate() != null) {
                record.headers().add(
                        new RecordHeader(
                                "tracestate",
                                event.getTracestate()
                                        .getBytes(StandardCharsets.UTF_8)
                        )
                );
            }

            kafkaTemplate
                    .send(record)
                    .whenComplete((result, exception) -> {

                        if (exception == null) {
                            event.markAsPublished();
                            repository.save(event);
                        }
                    });
        }
    }
}
