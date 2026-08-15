package org.ayachinene.app.order;

import io.vavr.control.Option;
import org.ayachinene.app.order.creation.OrderPos;
import org.ayachinene.app.order.creation.OrderQuantity;
import org.ayachinene.app.order.creation.ProductFacts;
import org.ayachinene.shared.uuid7.UUID7;

public interface OrderCreationRepository {

    Option<OrderQuantity> findOrderQuantity(
        UUID7 userId,
        String requestKey
    );

    Option<ProductFacts> findProductFacts(String skuCode);

    /**
     * 创建订单、订单项和库存预占，并扣减可用库存。
     * 调用方必须提供覆盖整个操作的事务。
     */
    void create(OrderPos orderPos);
}
