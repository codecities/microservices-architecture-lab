package com.architecturelab.orders.infrastructure.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class OrderJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal total;

    @Column(nullable = false)
    private String status;

    public OrderJpaEntity() {
    }

    public OrderJpaEntity(
            UUID id,
            Long customerId,
            BigDecimal total,
            String status
    ) {
        this.id = id;
        this.customerId = customerId;
        this.total = total;
        this.status = status;
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

    public String getStatus() {
        return status;
    }
}
