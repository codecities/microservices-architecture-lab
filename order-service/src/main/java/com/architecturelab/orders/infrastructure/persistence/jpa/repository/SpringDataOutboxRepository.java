package com.architecturelab.orders.infrastructure.persistence.jpa.repository;

import com.architecturelab.orders.infrastructure.persistence.jpa.OutboxEventJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataOutboxRepository
    extends JpaRepository<OutboxEventJpaEntity, UUID> {

    List<OutboxEventJpaEntity>
        findTop100ByPublishedAtIsNullOrderByOccurredAtAsc();
}
