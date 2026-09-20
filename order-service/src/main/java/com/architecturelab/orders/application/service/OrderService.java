package com.architecturelab.orders.application.service;

import com.architecturelab.orders.application.dto.CreateOrderRequest;
import com.architecturelab.orders.application.dto.OrderResponse;
import com.architecturelab.orders.application.event.OrderCreatedEvent;
import com.architecturelab.orders.application.exception.OrderNotFoundException;
import com.architecturelab.orders.application.port.OrderEventPublisher;
import com.architecturelab.orders.application.port.OutboxEventRepository;
import com.architecturelab.orders.domain.model.Order;
import com.architecturelab.orders.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OutboxEventRepository outboxEventRepository;

    public OrderService(
            OrderRepository orderRepository,
            OutboxEventRepository outboxEventRepository
    ) {
        this.orderRepository = orderRepository;
        this.outboxEventRepository = outboxEventRepository;
    }

    @Transactional
    public OrderResponse create(CreateOrderRequest request) {

        Order order = Order.create(
                request.customerId(),
                request.total()
        );

        Order savedOrder = orderRepository.save(order);

        OrderCreatedEvent event = new OrderCreatedEvent(
                UUID.randomUUID(),
                savedOrder.getId(),
                savedOrder.getCustomerId(),
                savedOrder.getTotal(),
                Instant.now()
        );

        outboxEventRepository.save(event);

        return toResponse(savedOrder);
    }

    public OrderResponse findById(UUID id) {
        Order order = orderRepository
                .findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        return toResponse(order);
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getTotal(),
                order.getStatus().name()
        );
    }
}
