package com.architecturelab.payments.application.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID eventId,
        UUID orderId,
        Long customerId,
        BigDecimal total,
        Instant occurredAt
) {
}
