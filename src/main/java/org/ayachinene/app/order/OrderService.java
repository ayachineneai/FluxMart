package org.ayachinene.app.order;

import org.ayachinene.app.order.creation.CreateOrderRequest;
import org.ayachinene.app.order.creation.CreateOrderResult;
import org.ayachinene.app.order.creation.OrderQuantity;
import org.ayachinene.app.service.Tx;
import org.ayachinene.shared.exception.ValidationException;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class OrderService {

    private final OrderCreationRepository orderCreationRepository;
    private final Tx tx;
    private final ObjectMapper objectMapper;

    public OrderService(
        OrderCreationRepository orderCreationRepository,
        Tx tx,
        ObjectMapper objectMapper
    ) {
        this.orderCreationRepository = orderCreationRepository;
        this.tx = tx;
        this.objectMapper = objectMapper;
    }

    public CreateOrderResult createOrder(CreateOrderRequest request) {
        Orders.validate(request);
        return tx.run(() -> doCreateOrder(request));
    }

    private CreateOrderResult doCreateOrder(CreateOrderRequest request) {
        var existingOrder = orderCreationRepository.findExistingOrder(
            request.userId(),
            request.requestKey()
        );

        if (existingOrder.isDefined()) {
            var existing = existingOrder.get();
            if (!Orders.isIdempotentRetry(
                existing.quantity(),
                new OrderQuantity(request.skuCode(), request.quantity())
            )) throw new ValidationException("requestKey has been used with different order parameters");
            return existing.result();
        }

        var productFacts = orderCreationRepository
            .findProductFacts(request.skuCode())
            .getOrElseThrow(() -> new ValidationException("sku does not exist"));

        Orders.checkOrderable(productFacts, request.quantity());

        var orderPos = Orders.mkOrderPos(
            objectMapper,
            request,
            productFacts,
            Orders.prepareOrderCreationInfo()
        );

        return orderCreationRepository.create(orderPos);
    }

}
