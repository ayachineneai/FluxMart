package org.ayachinene.api;

import org.ayachinene.app.exception.ValidationException;
import org.ayachinene.app.file.domain.FileResourceNotFoundException;
import org.ayachinene.app.file.domain.FileUploadCannotBeConfirmedException;
import org.ayachinene.app.order.domain.OrderCannotBeCreatedException;
import org.ayachinene.app.order.domain.OrderIdempotencyConflictException;
import org.ayachinene.app.stock.reservation.InsufficientStockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(ValidationException exception) {
        return new ApiError("VALIDATION_ERROR", exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleUnreadableMessage() {
        return new ApiError("INVALID_REQUEST", "Request body is invalid");
    }

    @ExceptionHandler(FileResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleFileResourceNotFound(
            FileResourceNotFoundException exception
    ) {
        return new ApiError("FILE_NOT_FOUND", exception.getMessage());
    }

    @ExceptionHandler(FileUploadCannotBeConfirmedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleFileUploadCannotBeConfirmed(
            FileUploadCannotBeConfirmedException exception
    ) {
        return new ApiError(
                "FILE_UPLOAD_CANNOT_BE_CONFIRMED",
                exception.getMessage()
        );
    }

    @ExceptionHandler(OrderCannotBeCreatedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleOrderCannotBeCreated(
            OrderCannotBeCreatedException exception
    ) {
        return new ApiError("ORDER_CANNOT_BE_CREATED", exception.getMessage());
    }

    @ExceptionHandler(OrderIdempotencyConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleOrderIdempotencyConflict(
            OrderIdempotencyConflictException exception
    ) {
        return new ApiError("ORDER_IDEMPOTENCY_CONFLICT", exception.getMessage());
    }

    @ExceptionHandler(InsufficientStockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleInsufficientStock(
            InsufficientStockException exception
    ) {
        return new ApiError("INSUFFICIENT_STOCK", exception.getMessage());
    }
}
