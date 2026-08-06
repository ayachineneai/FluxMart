package org.ayachinene.api;

public record ApiError(
        String code,
        String message
) {
}
