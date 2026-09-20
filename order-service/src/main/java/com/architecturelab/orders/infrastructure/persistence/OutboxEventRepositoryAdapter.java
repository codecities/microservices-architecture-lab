package com.architecturelab.orders.infrastructure.persistence;

import com.architecturelab.orders.application.event.OrderCreatedEvent;
import com.architecturelab.orders.application.port.OutboxEventRepository;
import com.architecturelab.orders.infrastructure.persistence.jpa.OutboxEventJpaEntity;
import com.architecturelab.orders.infrastructure.persistence.jpa.repository.SpringDataOutboxRepository;
import org.springframework.stereotype.Repository;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Repository
public class OutboxEventRepositoryAdapter
    implements OutboxEventRepository {

    private final SpringDataOutboxRepository repository;
    private final ObjectMapper objectMapper;

    public OutboxEventRepositoryAdapter(
            SpringDataOutboxRepository repository,
            ObjectMapper objectMapper
    ) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void save(OrderCreatedEvent event) {

        try {

            String payload = objectMapper.writeValueAsString(event);

            OutboxEventJpaEntity entity =
                    new OutboxEventJpaEntity(
                        event.eventId(),
                        event.orderId(),
                        "Order",
                        "OrderCreated",
                        payload,
                        event.occurredAt()
                    );

            repository.save(entity);
        } catch (JacksonException exception) {
            throw new IllegalStateException(
                    "Could not serialize outbox event",
                    exception
            );
        }
    }
}
