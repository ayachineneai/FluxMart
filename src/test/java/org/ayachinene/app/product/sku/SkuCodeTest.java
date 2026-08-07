package org.ayachinene.app.product.sku;

import org.ayachinene.app.domain.product.sku.SkuCode;
import org.ayachinene.app.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SkuCodeTest {

    @Test
    void generatesSkuCodeWithSkuPrefix() {
        assertTrue(SkuCode.generate().value().startsWith("SKU_"));
    }

    @Test
    void rejectsAnotherCodeTypePrefix() {
        assertThrows(
                ValidationException.class,
                () -> new SkuCode("PRD_23456789ABCDEFGHJKMN")
        );
    }
}
