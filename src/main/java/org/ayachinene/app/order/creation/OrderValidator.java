package org.ayachinene.app.order.creation;

import org.ayachinene.api.order.data.CreateOrderRequest;
import org.ayachinene.app.product.domain.sku.SkuCode;

import static org.ayachinene.utils.Validates.notNull;
import static org.ayachinene.utils.Validates.require;
import static org.ayachinene.utils.Validates.text;

public final class OrderValidator {

    private static final int MAX_QUANTITY = 99;

    private OrderValidator() {
    }

    public static CreateOrderRequest validate(CreateOrderRequest request) {
        notNull(request, "request");
        text(request.requestKey(), "requestKey", 64);
        new SkuCode(request.skuCode());
        require(
            request.quantity() > 0 && request.quantity() <= MAX_QUANTITY,
            "quantity must be between 1 and " + MAX_QUANTITY
        );
        return request;
    }
}
