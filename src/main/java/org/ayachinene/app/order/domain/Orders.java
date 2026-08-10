package org.ayachinene.app.order.domain;

import org.ayachinene.app.domain.money.Money;
import org.ayachinene.app.order.creation.CreateOrderInput;
import org.ayachinene.app.order.creation.CreateOrderItemInput;
import org.ayachinene.app.order.query.PurchasableSku;
import org.ayachinene.app.product.domain.ProductStatus;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.app.product.domain.sku.SkuStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Orders {

    private Orders() {
    }

    public static Order create(
            CreateOrderInput input,
            List<PurchasableSku> skus,
            OffsetDateTime paymentExpiresAt
    ) {
        var skuByCode = skus.stream().collect(Collectors.toMap(
                PurchasableSku::skuCode,
                Function.identity()
        ));
        var items = input.items().stream()
                .map(item -> createItem(item, skuByCode))
                .toList();
        return new Order(
                OrderCode.generate(),
                input.userId(),
                input.requestKey(),
                OrderStatus.PENDING_PAYMENT,
                totalAmount(items),
                paymentExpiresAt,
                items
        );
    }

    private static OrderItem createItem(
            CreateOrderItemInput input,
            Map<SkuCode, PurchasableSku> skuByCode
    ) {
        var sku = skuByCode.get(input.skuCode());
        if (sku == null) {
            throw cannotCreate("SKU does not exist: " + input.skuCode().value());
        }
        if (sku.productStatus() != ProductStatus.ON_SALE) {
            throw cannotCreate("product is not on sale: " + sku.productCode().value());
        }
        if (sku.skuStatus() != SkuStatus.ENABLED) {
            throw cannotCreate("SKU is disabled: " + sku.skuCode().value());
        }
        var totalAmount = new Money(
                sku.price().amount().multiply(BigDecimal.valueOf(input.quantity()))
        );
        return new OrderItem(
                sku.productCode(),
                sku.skuCode(),
                sku.productTitle(),
                sku.specifications(),
                sku.imageFileId(),
                sku.price(),
                input.quantity(),
                totalAmount
        );
    }

    private static Money totalAmount(List<OrderItem> items) {
        var amount = items.stream()
                .map(item -> item.totalAmount().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Money(amount);
    }

    public static void requireConsistent(Order existing, CreateOrderInput input) {
        if (!itemQuantities(existing).equals(itemQuantities(input))) {
            throw new OrderIdempotencyConflictException(input.requestKey());
        }
    }

    private static Map<SkuCode, Integer> itemQuantities(Order order) {
        return order.items().stream().collect(Collectors.toMap(
                OrderItem::skuCode,
                OrderItem::quantity
        ));
    }

    private static Map<SkuCode, Integer> itemQuantities(CreateOrderInput input) {
        return input.items().stream().collect(Collectors.toMap(
                CreateOrderItemInput::skuCode,
                CreateOrderItemInput::quantity
        ));
    }

    private static OrderCannotBeCreatedException cannotCreate(String reason) {
        return new OrderCannotBeCreatedException(reason);
    }
}
