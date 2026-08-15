package org.ayachinene.app.product.domain.sku;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SkuCodeTest {

    @Test
    void constructorAndExternalValidationUseTheSameRules() {
        var value = SkuCode.generate().value();

        assertDoesNotThrow(() -> SkuCode.validate(value));
        assertDoesNotThrow(() -> new SkuCode(value));
        assertThrows(
            IllegalArgumentException.class,
            () -> SkuCode.validate("invalid")
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> new SkuCode("invalid")
        );
    }
}
