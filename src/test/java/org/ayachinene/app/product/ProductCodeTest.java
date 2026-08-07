package org.ayachinene.app.product;

import org.ayachinene.app.domain.product.ProductCode;
import org.ayachinene.app.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductCodeTest {

    @Test
    void generatesProductCodeWithProductPrefix() {
        assertTrue(ProductCode.generate().value().startsWith("PRD_"));
    }

    @Test
    void rejectsAnotherCodeTypePrefix() {
        assertThrows(
                ValidationException.class,
                () -> new ProductCode("SKU_23456789ABCDEFGHJKMN")
        );
    }
}
