package com.architecturelab.orders.application.dto;

import java.math.BigDecimal;

public record CreateOrderRequest(
        Long customerId,
        BigDecimal total
) {
}
