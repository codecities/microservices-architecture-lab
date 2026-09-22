package com.architecturelab.payments.domain.model.repository;

import com.architecturelab.payments.domain.model.Payment;

import java.util.UUID;

public interface PaymentRepository {

    Payment save(Payment payment);

    boolean existsByOrderId(UUID id);
}
