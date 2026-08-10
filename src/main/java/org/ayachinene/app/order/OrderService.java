package org.ayachinene.app.order;

import org.ayachinene.app.order.creation.CreateOrderInput;
import org.ayachinene.app.order.creation.CreateOrderItemInput;
import org.ayachinene.app.order.creation.CreateOrderResult;
import org.ayachinene.app.order.creation.CreateOrderUseCase;
import org.ayachinene.app.order.creation.OrderValidator;
import org.ayachinene.app.order.domain.Order;
import org.ayachinene.app.order.domain.Orders;
import org.ayachinene.app.order.repository.OrderRepository;
import org.ayachinene.app.order.repository.PurchasableSkuRepository;
import org.ayachinene.app.order.repository.OrderUniquenessConflictException;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.app.service.Tx;
import org.ayachinene.app.stock.repository.StockRepository;
import org.ayachinene.app.stock.reservation.ReserveStockItem;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService implements CreateOrderUseCase {

    private static final long PAYMENT_TIMEOUT_MINUTES = 15L;

    private final OrderRepository orderRepository;
    private final PurchasableSkuRepository purchasableSkuRepository;
    private final StockRepository stockRepository;
    private final Tx tx;

    public OrderService(
            OrderRepository orderRepository,
            PurchasableSkuRepository purchasableSkuRepository,
            StockRepository stockRepository,
            Tx tx
    ) {
        this.orderRepository = orderRepository;
        this.purchasableSkuRepository = purchasableSkuRepository;
        this.stockRepository = stockRepository;
        this.tx = tx;
    }

    @Override
    public CreateOrderResult createOrder(CreateOrderInput input) {
        var validatedInput = OrderValidator.validate(input);
        try {
            return tx.run(() -> doCreateOrder(validatedInput));
        } catch (OrderUniquenessConflictException exception) {
            return tx.run(() -> recoverExistingOrder(validatedInput, exception));
        }
    }

    private CreateOrderResult recoverExistingOrder(
            CreateOrderInput input,
            OrderUniquenessConflictException conflict
    ) {
        var order = orderRepository.findByUserIdAndRequestKey(
                input.userId(),
                input.requestKey()
        ).orElseThrow(() -> conflict);
        Orders.requireConsistent(order, input);
        return result(order);
    }

    private CreateOrderResult doCreateOrder(CreateOrderInput input) {
        var existingOrder = orderRepository.findByUserIdAndRequestKey(
                input.userId(),
                input.requestKey()
        );
        if (existingOrder.isPresent()) {
            var order = existingOrder.get();
            Orders.requireConsistent(order, input);
            return result(order);
        }

        var skus = purchasableSkuRepository.findBySkuCodes(skuCodes(input));
        var order = Orders.create(
                input,
                skus,
                OffsetDateTime.now().plusMinutes(PAYMENT_TIMEOUT_MINUTES)
        );
        orderRepository.create(order);
        reserveStock(order);
        return result(order);
    }

    private Set<SkuCode> skuCodes(CreateOrderInput input) {
        return input.items().stream()
                .map(CreateOrderItemInput::skuCode)
                .collect(Collectors.toSet());
    }

    private void reserveStock(Order order) {
        stockRepository.reserve(
                order.orderCode(),
                order.paymentExpiresAt(),
                stockItems(order)
        );
    }

    private List<ReserveStockItem> stockItems(Order order) {
        return order.items().stream()
                .map(item -> new ReserveStockItem(
                        item.skuCode(),
                        item.quantity()
                ))
                .toList();
    }

    private CreateOrderResult result(Order order) {
        return new CreateOrderResult(
                order.orderCode(),
                order.status(),
                order.totalAmount(),
                order.paymentExpiresAt()
        );
    }
}
