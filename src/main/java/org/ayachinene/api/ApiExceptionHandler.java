package org.ayachinene.api;

import org.ayachinene.app.exception.ValidationException;
import org.ayachinene.app.file.domain.FileResourceNotFoundException;
import org.ayachinene.app.file.domain.FileUploadCannotBeConfirmedException;
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
}
