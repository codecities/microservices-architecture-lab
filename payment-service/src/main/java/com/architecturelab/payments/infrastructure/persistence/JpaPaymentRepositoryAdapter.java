package com.architecturelab.payments.infrastructure.persistence;

import com.architecturelab.payments.domain.model.Payment;
import com.architecturelab.payments.domain.model.repository.PaymentRepository;
import com.architecturelab.payments.infrastructure.persistence.jpa.PaymentJpaEntity;
import com.architecturelab.payments.infrastructure.persistence.jpa.SpringDataPaymentRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class JpaPaymentRepositoryAdapter
    implements PaymentRepository {

    private final SpringDataPaymentRepository repository;

    public JpaPaymentRepositoryAdapter(
            SpringDataPaymentRepository repository
    ){
        this.repository = repository;
    }

    @Override
    public Payment save(Payment payment) {

        PaymentJpaEntity entity = new PaymentJpaEntity(
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getStatus().toString(),
                payment.getCreatedAt()
        );

        repository.save(entity);

        return payment;
    }

    public boolean existsByOrderId(UUID id) {

        return repository.existsByOrderId(id);
    }
}
