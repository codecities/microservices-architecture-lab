package com.architecturelab.orders.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataOrderRepository
    extends JpaRepository<OrderJpaEntity, UUID> {
}
