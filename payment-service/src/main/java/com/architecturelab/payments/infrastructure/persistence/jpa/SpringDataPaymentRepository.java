package com.architecturelab.payments.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataPaymentRepository
    extends JpaRepository<PaymentJpaEntity, UUID> {

    boolean existsByOrderId(UUID id);
}
