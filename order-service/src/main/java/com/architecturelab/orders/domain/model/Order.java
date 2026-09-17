package com.architecturelab.orders.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Order {

    private final UUID id;
    private final Long customerId;

    private final BigDecimal total;
    private final OrderStatus status;

    public Order(
            UUID id,
            Long customerId,
            BigDecimal total,
            OrderStatus status
    ){
        this.id = id;
        this.customerId = customerId;
        this.total = total;
        this.status = status;
    }

    public static Order create(Long customerId, BigDecimal total) {
        return new Order(
                UUID.randomUUID(),
                customerId,
                total,
                OrderStatus.CREATED
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
}
