package org.ayachinene.api.order;

import org.ayachinene.api.order.data.CreateOrderRequest;
import org.ayachinene.api.order.data.CreateOrderResponse;
import org.ayachinene.app.order.creation.CreateOrderInput;
import org.ayachinene.app.order.creation.CreateOrderItemInput;
import org.ayachinene.app.order.creation.CreateOrderResult;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.shared.uuid7.UUID7;
import org.ayachinene.utils.Streams;
import org.ayachinene.utils.Validates;
import org.ayachinene.utils.Values;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderApiMapper {

    private static final int MAX_REQUEST_KEY_LENGTH = 64;

    public CreateOrderInput toInput(
            UUID7 userId,
            String requestKey,
            CreateOrderRequest request
    ) {
        return new CreateOrderInput(
                userId,
                Validates.requiredText(
                        requestKey,
                        "Idempotency-Key",
                        MAX_REQUEST_KEY_LENGTH
                ),
                items(request.items())
        );
    }

    public CreateOrderResponse toResponse(CreateOrderResult result) {
        return new CreateOrderResponse(
                result.orderCode().value(),
                result.status().name(),
                result.totalAmount().amount(),
                result.paymentExpiresAt()
        );
    }

    private List<CreateOrderItemInput> items(
            List<CreateOrderRequest.ItemRequest> requests
    ) {
        return Streams.of(requests)
                .map(this::item)
                .toList();
    }

    private CreateOrderItemInput item(CreateOrderRequest.ItemRequest request) {
        return Values.map(
                request,
                value -> new CreateOrderItemInput(
                        new SkuCode(value.skuCode()),
                        value.quantity()
                )
        );
    }
}
