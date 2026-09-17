package com.architecturelab.orders.application.service;

import com.architecturelab.orders.application.dto.CreateOrderRequest;
import com.architecturelab.orders.application.dto.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderService {

    public OrderResponse create(CreateOrderRequest request) {

        return new OrderResponse(
                UUID.randomUUID(),
                request.customerId(),
                request.total(),
                "CREATED"
        );
    }
}
