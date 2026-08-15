package org.ayachinene.app.order;

import io.vavr.control.Option;
import org.ayachinene.app.code.BusinessCodes;
import org.ayachinene.app.order.creation.CreateOrderRequest;
import org.ayachinene.app.order.creation.OrderCreationInfo;
import org.ayachinene.app.order.creation.OrderPos;
import org.ayachinene.app.order.creation.OrderQuantity;
import org.ayachinene.app.order.creation.ProductFacts;
import org.ayachinene.app.order.domain.OrderStatus;
import org.ayachinene.app.order.domain.StockReservationStatus;
import org.ayachinene.app.product.domain.ProductStatus;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.app.product.domain.sku.SkuStatus;
import org.ayachinene.infra.order.persistence.CustomerOrderPO;
import org.ayachinene.infra.order.persistence.OrderItemPO;
import org.ayachinene.infra.order.persistence.StockReservationPO;
import org.ayachinene.shared.exception.ValidationException;
import org.ayachinene.shared.uuid7.UUID7s;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.ayachinene.utils.Validates.notNull;
import static org.ayachinene.utils.Validates.require;
import static org.ayachinene.utils.Validates.text;

public final class Orders {

    private static final String ORDER_CODE_PREFIX = "ORD_";
    private static final Duration PAYMENT_TIMEOUT = Duration.ofMinutes(15);
    private static final int MAX_REQUEST_KEY_LENGTH = 64;
    private static final int MAX_QUANTITY = 99;

    private Orders() {
    }

    public static void validate(CreateOrderRequest request) {
        notNull(request, "request");
        text(request.requestKey(), "requestKey", MAX_REQUEST_KEY_LENGTH);
        notNull(request.userId(), "userId");
        skuCode(request.skuCode());
        require(
            request.quantity() > 0 && request.quantity() <= MAX_QUANTITY,
            "quantity must be between 1 and " + MAX_QUANTITY
        );
    }

    public static boolean isIdempotentRetry(
        Option<OrderQuantity> existingOrderQuantity,
        OrderQuantity requestedOrderQuantity
    ) {
        return existingOrderQuantity.exists(x ->
            Objects.equals(x, requestedOrderQuantity)
        );
    }

    public static void checkOrderable(
        ProductFacts productFacts,
        int quantity
    ) {
        var baseInfo = productFacts.baseInfo();
        if (baseInfo.productStatus() != ProductStatus.ON_SALE) {
            throw new ValidationException("product is not on sale");
        }
        if (baseInfo.skuStatus() != SkuStatus.ENABLED) {
            throw new ValidationException("sku is not enabled");
        }
        if (baseInfo.availableQuantity() < quantity) {
            throw new ValidationException("insufficient stock");
        }
    }

    public static OrderCreationInfo prepareOrderCreationInfo() {
        var createdAt = LocalDateTime.now();
        return new OrderCreationInfo(
            UUID7s.generate(),
            UUID7s.generate(),
            UUID7s.generate(),
            ORDER_CODE_PREFIX + BusinessCodes.generate(),
            createdAt,
            createdAt.plus(PAYMENT_TIMEOUT)
        );
    }

    public static OrderPos mkOrderPos(
        ObjectMapper objectMapper,
        CreateOrderRequest request,
        ProductFacts productFacts,
        OrderCreationInfo creationInfo
    ) {
        var baseInfo = productFacts.baseInfo();
        var totalAmount = Math.multiplyExact(
            baseInfo.unitPriceAmount(),
            request.quantity()
        );
        var customerOrder = new CustomerOrderPO(
            creationInfo.orderId(),
            creationInfo.orderCode(),
            request.userId(),
            request.requestKey(),
            OrderStatus.PENDING_PAYMENT,
            totalAmount,
            creationInfo.paymentExpiresAt(),
            0L,
            creationInfo.createdAt(),
            creationInfo.createdAt()
        );
        var orderItem = new OrderItemPO(
            creationInfo.orderItemId(),
            creationInfo.orderId(),
            baseInfo.productCode(),
            baseInfo.skuCode(),
            baseInfo.productTitle(),
            objectMapper.writeValueAsString(
                productFacts.specificationSelections()
            ),
            baseInfo.snapshotImageFileId(),
            baseInfo.unitPriceAmount(),
            request.quantity(),
            totalAmount,
            0,
            creationInfo.createdAt()
        );
        var stockReservation = new StockReservationPO(
            creationInfo.stockReservationId(),
            creationInfo.orderId(),
            creationInfo.orderItemId(),
            baseInfo.skuId(),
            request.quantity(),
            StockReservationStatus.RESERVED,
            creationInfo.paymentExpiresAt(),
            creationInfo.createdAt(),
            creationInfo.createdAt()
        );
        return new OrderPos(
            baseInfo.stockId(),
            customerOrder,
            orderItem,
            stockReservation
        );
    }

    private static void skuCode(String skuCode) {
        try {
            SkuCode.validate(skuCode);
        } catch (IllegalArgumentException exception) {
            throw new ValidationException("skuCode is invalid");
        }
    }
}
