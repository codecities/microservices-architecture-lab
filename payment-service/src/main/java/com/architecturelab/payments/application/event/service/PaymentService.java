package com.architecturelab.payments.application.event.service;

import com.architecturelab.payments.application.event.OrderCreatedEvent;
import com.architecturelab.payments.domain.model.Payment;
import com.architecturelab.payments.domain.model.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository repository;

    public PaymentService(
            PaymentRepository repository
    ) {
        this.repository = repository;
    }

    @Transactional
    public void createPayment(OrderCreatedEvent event) {

        if (repository.existsByOrderId(event.orderId())) {
            return;
        }

        Payment payment = Payment.create(
                event.orderId(),
                event.total()
        );

        repository.save(payment);
    }
}
