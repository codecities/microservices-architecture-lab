package com.architecturelab.payments.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Payment {

    private final UUID id;
    private final UUID orderId;
    private final BigDecimal amount;
    private final PaymentStatus status;
    private final Instant createdAt;

    public Payment(
            UUID id,
            UUID orderId,
            BigDecimal amount,
            PaymentStatus status,
            Instant createdAt
    ) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static Payment create(
            UUID orderId,
            BigDecimal amount
    ) {
        return new Payment(
                UUID.randomUUID(),
                orderId,
                amount,
                PaymentStatus.PENDING,
                Instant.now()
        );
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
