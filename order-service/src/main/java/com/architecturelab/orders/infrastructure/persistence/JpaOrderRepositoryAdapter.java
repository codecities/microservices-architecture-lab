package com.architecturelab.orders.infrastructure.persistence;

import com.architecturelab.orders.domain.model.Order;
import com.architecturelab.orders.domain.model.OrderStatus;
import com.architecturelab.orders.domain.repository.OrderRepository;
import com.architecturelab.orders.infrastructure.persistence.jpa.OrderJpaEntity;
import com.architecturelab.orders.infrastructure.persistence.jpa.repository.SpringDataOrderRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaOrderRepositoryAdapter implements OrderRepository {

    private final SpringDataOrderRepository repository;

    public JpaOrderRepositoryAdapter(
            SpringDataOrderRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public Order save(Order order) {
        OrderJpaEntity entity = new OrderJpaEntity(
                order.getId(),
                order.getCustomerId(),
                order.getTotal(),
                order.getStatus().name(),
                order.getCreatedAt()
        );

        OrderJpaEntity saved = repository.save(entity);

        return new Order(
                saved.getId(),
                saved.getCustomerId(),
                saved.getTotal(),
                OrderStatus.valueOf(entity.getStatus()),
                saved.getCreatedAt()
        );
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return repository
                .findById(id)
                .map(
                entity ->
                        new Order(
                                entity.getId(),
                                entity.getCustomerId(),
                                entity.getTotal(),
                                OrderStatus.valueOf(entity.getStatus()),
                                entity.getCreatedAt()
                        )
                );
    }
}
