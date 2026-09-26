package com.architecturelab.payments.infrastructure.messaging.kafka;

import com.architecturelab.payments.application.event.OrderCreatedEvent;
import com.architecturelab.payments.application.service.PaymentService;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapGetter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrderCreatedConsumer {

    private final ObjectMapper objectMapper;
    private final PaymentService paymentService;
    private final MeterRegistry meterRegistry;
    private final OpenTelemetry openTelemetry;

    public OrderCreatedConsumer(
            ObjectMapper objectMapper,
            PaymentService paymentService,
            MeterRegistry meterRegistry,
            OpenTelemetry openTelemetry
    ) {
        this.objectMapper = objectMapper;
        this.paymentService = paymentService;
        this.meterRegistry = meterRegistry;
        this.openTelemetry = openTelemetry;
    }

    @KafkaListener(
            topics = "orders.created",
            groupId = "payment-service"
    )
    public void consume(
            String payload,
            @Header(name = "traceparent", required = false)
            String traceparent,
            @Header(name = "tracestate", required = false)
            String tracestate
    ) throws JacksonException {

        Map<String, String> carrier = new HashMap<>();

        if (traceparent != null) {
            carrier.put("traceparent", traceparent);
        }

        if (tracestate != null) {
            carrier.put("tracestate", tracestate);
        }

        TextMapGetter<Map<String, String>> getter =
                new TextMapGetter<Map<String, String>>() {
                    @Override
                    public Iterable<String> keys(Map<String, String> carrier) {
                        return carrier.keySet();
                    }

                    @Override
                    public String get(Map<String, String> carrier, String key) {
                        return carrier == null ? null : carrier.get(key);
                    }
                };

        Context extratedContext =
                openTelemetry
                        .getPropagators()
                        .getTextMapPropagator()
                        .extract(
                                Context.current(),
                                carrier,
                                getter
                        );

        Tracer tracer =
                openTelemetry.getTracer(
                        "com.architecturelab.payments"
                );

        Span span =
                tracer
                        .spanBuilder("Process OrderCreated")
                        .setParent(extratedContext)
                        .startSpan();

        try (Scope scope = span.makeCurrent()) {
            OrderCreatedEvent event =
                    objectMapper.readValue(
                            payload,
                            OrderCreatedEvent.class
                    );

            paymentService.createPayment(event);
        } catch (Exception exception) {

            span.recordException(exception);
            span.setStatus(StatusCode.ERROR);

            throw new RuntimeException(exception);
        } finally {

            span.end();
        }
    }
}
