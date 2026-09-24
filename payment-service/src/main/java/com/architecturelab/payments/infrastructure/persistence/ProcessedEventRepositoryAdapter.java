package com.architecturelab.payments.infrastructure.persistence;

import com.architecturelab.payments.application.port.ProcessedEventRepository;
import com.architecturelab.payments.infrastructure.persistence.jpa.ProcessedEventJpaEntity;
import com.architecturelab.payments.infrastructure.persistence.jpa.SpringDataProcessedEventRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

@Repository
public class ProcessedEventRepositoryAdapter
    implements ProcessedEventRepository {

    private final SpringDataProcessedEventRepository repository;
    private final MeterRegistry meterRegistry;

    public ProcessedEventRepositoryAdapter(
            SpringDataProcessedEventRepository repository,
            MeterRegistry meterRegistry
    ) {
        this.repository = repository;
        this.meterRegistry = meterRegistry;
    }

    @Override
    public boolean exists(UUID eventId) {
        return repository.existsById(eventId);
    }

    @Override
    public void save(UUID id, String eventType) {

        ProcessedEventJpaEntity entity =
                new ProcessedEventJpaEntity(
                        id,
                        eventType,
                        Instant.now()
                );
        repository.save(entity);

        meterRegistry
                .counter("payment.events.recieved")
                .increment();
    }
}
