package org.ayachinene.app.domain.product;

public record CategoryCode(String value) {
    public CategoryCode {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("categoryCode must not be blank");
        }
        value = value.trim();
    }
}
