package org.ayachinene.app.order;

import org.ayachinene.app.domain.money.Money;
import org.ayachinene.app.order.creation.CreateOrderInput;
import org.ayachinene.app.order.creation.CreateOrderItemInput;
import org.ayachinene.app.order.creation.CreateOrderResult;
import org.ayachinene.app.order.domain.Order;
import org.ayachinene.app.order.query.PurchasableSku;
import org.ayachinene.app.order.repository.OrderRepository;
import org.ayachinene.app.order.repository.PurchasableSkuRepository;
import org.ayachinene.app.order.repository.OrderUniquenessConflictException;
import org.ayachinene.app.order.domain.Orders;
import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.domain.ProductStatus;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.app.product.domain.sku.SkuStatus;
import org.ayachinene.app.service.Tx;
import org.ayachinene.app.stock.repository.StockRepository;
import org.ayachinene.shared.uuid7.UUID7s;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class OrderServiceTest {

    @Test
    void createsOrderAndReservesStockInOneTransaction() {
        var orderRepository = mock(OrderRepository.class);
        var skuRepository = mock(PurchasableSkuRepository.class);
        var stockRepository = mock(StockRepository.class);
        var tx = mock(Tx.class);
        var sku = purchasableSku();
        var input = new CreateOrderInput(
                UUID7s.generate(),
                "request-001",
                List.of(new CreateOrderItemInput(sku.skuCode(), 2))
        );
        when(orderRepository.findByUserIdAndRequestKey(
                input.userId(),
                input.requestKey()
        )).thenReturn(Optional.empty());
        when(skuRepository.findBySkuCodes(any())).thenReturn(List.of(sku));
        when(tx.run(org.mockito.ArgumentMatchers.<Callable<CreateOrderResult>>any()))
                .thenAnswer(invocation ->
                        invocation.<Callable<CreateOrderResult>>getArgument(0).call()
                );
        var service = new OrderService(
                orderRepository,
                skuRepository,
                stockRepository,
                tx
        );

        var result = service.createOrder(input);

        var orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).create(orderCaptor.capture());
        var order = orderCaptor.getValue();
        assertEquals(order.orderCode(), result.orderCode());
        assertEquals(new BigDecimal("39.80"), result.totalAmount().amount());
        verify(stockRepository).reserve(
                org.mockito.ArgumentMatchers.eq(order.orderCode()),
                org.mockito.ArgumentMatchers.eq(order.paymentExpiresAt()),
                any()
        );
        verify(tx).run(org.mockito.ArgumentMatchers.<Callable<CreateOrderResult>>any());
    }

    @Test
    void recoversAnOrderCreatedByAConcurrentRequestInANewTransaction() {
        var orderRepository = mock(OrderRepository.class);
        var skuRepository = mock(PurchasableSkuRepository.class);
        var stockRepository = mock(StockRepository.class);
        var tx = mock(Tx.class);
        var sku = purchasableSku();
        var input = new CreateOrderInput(
                UUID7s.generate(),
                "request-001",
                List.of(new CreateOrderItemInput(sku.skuCode(), 1))
        );
        var existingOrder = Orders.create(
                input,
                List.of(sku),
                OffsetDateTime.now().plusMinutes(15)
        );
        when(orderRepository.findByUserIdAndRequestKey(
                input.userId(),
                input.requestKey()
        )).thenReturn(Optional.empty(), Optional.of(existingOrder));
        when(skuRepository.findBySkuCodes(any())).thenReturn(List.of(sku));
        doThrow(new OrderUniquenessConflictException(new RuntimeException()))
                .when(orderRepository).create(any(Order.class));
        when(tx.run(org.mockito.ArgumentMatchers.<Callable<CreateOrderResult>>any()))
                .thenAnswer(invocation ->
                        invocation.<Callable<CreateOrderResult>>getArgument(0).call()
                );
        var service = new OrderService(
                orderRepository,
                skuRepository,
                stockRepository,
                tx
        );

        var result = service.createOrder(input);

        assertEquals(existingOrder.orderCode(), result.orderCode());
        verify(tx, times(2)).run(
                org.mockito.ArgumentMatchers.<Callable<CreateOrderResult>>any()
        );
        verifyNoInteractions(stockRepository);
    }

    private PurchasableSku purchasableSku() {
        return new PurchasableSku(
                UUID7s.generate(),
                ProductCode.generate(),
                SkuCode.generate(),
                ProductStatus.ON_SALE,
                SkuStatus.ENABLED,
                "FluxMart product",
                List.of(),
                UUID7s.generate(),
                new Money(new BigDecimal("19.90"))
        );
    }
}
