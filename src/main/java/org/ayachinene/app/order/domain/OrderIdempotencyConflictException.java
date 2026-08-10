package org.ayachinene.app.order.domain;

public class OrderIdempotencyConflictException extends RuntimeException {

    public OrderIdempotencyConflictException(String requestKey) {
        super("Idempotency key was already used by a different order request: " + requestKey);
    }
}
