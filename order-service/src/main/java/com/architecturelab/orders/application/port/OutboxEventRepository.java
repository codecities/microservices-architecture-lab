package com.architecturelab.orders.application.port;

import com.architecturelab.orders.application.event.OrderCreatedEvent;

public interface OutboxEventRepository {

    void save(OrderCreatedEvent event);
}
