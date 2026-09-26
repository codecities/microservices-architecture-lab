package com.architecturelab.orders.infrastructure.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
public class OutboxEventJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID aggregateId;

    @Column(nullable = false)
    private String aggregateType;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false)
    private Instant occurredAt;

    @Column
    private Instant publishedAt;

    @Column
    private String traceparent;

    @Column
    private String tracestate;

    public OutboxEventJpaEntity () {
    }

    public OutboxEventJpaEntity(
            UUID id,
            UUID aggregateId,
            String aggregateType,
            String eventType,
            String payload,
            Instant occurredAt,
            String traceparent,
            String tracestate
    ) {
        this.id = id;
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
        this.eventType = eventType;
        this.payload = payload;
        this.occurredAt = occurredAt;
        this.traceparent = traceparent;
        this.tracestate = tracestate;
    }

    public UUID getId() {
        return id;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public String getEventType() {
        return eventType;
    }

    public String getPayload() {
        return payload;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public String getTraceparent() {
        return traceparent;
    }

    public String getTracestate() {
        return tracestate;
    }

    public void markAsPublished() {
        this.publishedAt = Instant.now();
    }
}
