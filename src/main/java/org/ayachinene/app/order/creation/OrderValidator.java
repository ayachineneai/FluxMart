package org.ayachinene.app.order.creation;

import org.ayachinene.utils.Lists;
import org.ayachinene.utils.Validates;

public final class OrderValidator {

    private static final int MAX_REQUEST_KEY_LENGTH = 64;
    private static final int MAX_ORDER_ITEMS = 100;

    private OrderValidator() {
    }

    public static CreateOrderInput validate(CreateOrderInput input) {
        var items = Lists.nullToEmpty(input.items()).stream()
                .map(OrderValidator::item)
                .toList();
        Validates.require(!items.isEmpty(), "order items must not be empty");
        Validates.require(
                items.size() <= MAX_ORDER_ITEMS,
                "order must not contain more than 100 items"
        );
        Validates.require(
                Lists.isUnique(items, CreateOrderItemInput::skuCode),
                "order items must not contain duplicated skuCode"
        );
        return new CreateOrderInput(
                input.userId(),
                Validates.requiredText(
                        input.requestKey(),
                        "requestKey",
                        MAX_REQUEST_KEY_LENGTH
                ),
                items
        );
    }

    private static CreateOrderItemInput item(CreateOrderItemInput input) {
        Validates.requireNonNull(input, "order item");
        Validates.requireNonNull(input.skuCode(), "skuCode");
        var quantity = Validates.requireNonNull(input.quantity(), "quantity");
        Validates.require(quantity > 0, "quantity must be greater than zero");
        return new CreateOrderItemInput(input.skuCode(), quantity);
    }
}
