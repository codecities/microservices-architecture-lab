package com.architecturelab.orders.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        Long customerId,
        BigDecimal total,
        String status
) {
}
