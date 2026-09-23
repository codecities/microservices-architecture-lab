package com.architecturelab.payments.application.service;

import com.architecturelab.payments.application.event.OrderCreatedEvent;
import com.architecturelab.payments.application.port.ProcessedEventRepository;
import com.architecturelab.payments.domain.model.Payment;
import com.architecturelab.payments.domain.model.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ProcessedEventRepository processedEventRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            ProcessedEventRepository processedEventRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.processedEventRepository = processedEventRepository;
    }

    @Transactional
    public void createPayment(OrderCreatedEvent event) {

        if (processedEventRepository.exists(event.eventId())) {
            return;
        }

        Payment payment = Payment.create(
                event.orderId(),
                event.total()
        );

        processedEventRepository.save(
                event.eventId(),
                "OrderCreated"
        );
        paymentRepository.save(payment);
    }
}
