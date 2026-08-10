package org.ayachinene.app.order.repository;

import org.ayachinene.app.order.domain.Order;
import org.ayachinene.shared.uuid7.UUID7;

import java.util.Optional;

public interface OrderRepository {

    Optional<Order> findByUserIdAndRequestKey(UUID7 userId, String requestKey);

    void create(Order order);
}
