package com.architecturelab.orders.application.port;

import com.architecturelab.orders.application.event.OrderCreatedEvent;

public interface OrderEventPublisher {

    void publish(OrderCreatedEvent event);
}
