package org.ayachinene.app.domain.product.sku;

import org.ayachinene.app.uuid7.UUID7;

import java.util.Objects;

public record SkuCode(UUID7 value) {

    public SkuCode {
        Objects.requireNonNull(value, "value must not be null");
    }
}
