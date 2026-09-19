package com.architecturelab.orders.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Order {

    private final UUID id;
    private final Long customerId;

    private final BigDecimal total;
    private final OrderStatus status;

    private final Instant createdAt;

    public Order(
            UUID id,
            Long customerId,
            BigDecimal total,
            OrderStatus status,
            Instant createdAt
    ){
        this.id = id;
        this.customerId = customerId;
        this.total = total;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static Order create(Long customerId, BigDecimal total) {
        return new Order(
                UUID.randomUUID(),
                customerId,
                total,
                OrderStatus.CREATED,
                Instant.now()
        );
    }

    public UUID getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
