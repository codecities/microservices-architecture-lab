package com.architecturelab.payments.application.service;

import com.architecturelab.payments.application.event.OrderCreatedEvent;
import com.architecturelab.payments.application.port.ProcessedEventRepository;
import com.architecturelab.payments.domain.model.Payment;
import com.architecturelab.payments.domain.model.repository.PaymentRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ProcessedEventRepository processedEventRepository;
    private final MeterRegistry meterRegistry;

    public PaymentService(
            PaymentRepository paymentRepository,
            ProcessedEventRepository processedEventRepository,
            MeterRegistry meterRegister
    ) {
        this.paymentRepository = paymentRepository;
        this.processedEventRepository = processedEventRepository;
        this.meterRegistry = meterRegister;
    }

    @Transactional
    public void createPayment(OrderCreatedEvent event) {

        if (processedEventRepository.exists(event.eventId())) {

            meterRegistry
                    .counter("payments.events.duplicates")
                    .increment();

            return;
        }

        Payment payment = Payment.create(
                event.orderId(),
                event.total()
        );

        paymentRepository.save(payment);

        processedEventRepository.save(
                event.eventId(),
                "payments.created"
        );

        meterRegistry
                .counter("payments.created")
                .increment();
    }
}
