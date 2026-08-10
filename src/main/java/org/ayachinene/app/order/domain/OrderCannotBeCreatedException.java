package org.ayachinene.app.order.domain;

public class OrderCannotBeCreatedException extends RuntimeException {

    public OrderCannotBeCreatedException(String reason) {
        super("Order cannot be created: " + reason);
    }
}
