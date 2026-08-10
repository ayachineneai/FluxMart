package org.ayachinene.app.order.repository;

public class OrderUniquenessConflictException extends RuntimeException {

    public OrderUniquenessConflictException(Throwable cause) {
        super(cause);
    }
}
