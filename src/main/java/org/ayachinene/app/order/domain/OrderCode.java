package org.ayachinene.app.order.domain;

import org.ayachinene.app.code.BusinessCodes;

public record OrderCode(String value) {

    private static final String PREFIX = "ORD_";

    public OrderCode {
        value = BusinessCodes.validate(value, PREFIX, "orderCode");
    }

    public static OrderCode generate() {
        return new OrderCode(PREFIX + BusinessCodes.generateBody());
    }
}
