package org.ayachinene.app.product;

import org.ayachinene.app.exception.ValidationException;
import org.ayachinene.app.product.domain.specification.SpecificationCode;
import org.ayachinene.app.product.domain.specification.SpecificationValueCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpecificationCodeTest {

    @Test
    void generatesCodesWithDistinctPrefixes() {
        assertTrue(SpecificationCode.generate().value().startsWith("SPC_"));
        assertTrue(SpecificationValueCode.generate().value().startsWith("SPV_"));
    }

    @Test
    void rejectsUsingAValueCodeAsASpecificationCode() {
        var valueCode = SpecificationValueCode.generate();

        assertThrows(
                ValidationException.class,
                () -> new SpecificationCode(valueCode.value())
        );
    }
}
