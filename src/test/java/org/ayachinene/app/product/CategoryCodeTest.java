package org.ayachinene.app.product;

import org.ayachinene.app.product.domain.CategoryCode;
import org.ayachinene.app.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoryCodeTest {

    @Test
    void normalizesCategoryCode() {
        assertEquals("TSHIRT", new CategoryCode("  TSHIRT  ").value());
    }

    @Test
    void rejectsBlankCategoryCodeAsInvalidBusinessInput() {
        assertThrows(ValidationException.class, () -> new CategoryCode("  "));
    }

    @Test
    void rejectsNullCategoryCodeAsInvalidBusinessInput() {
        assertThrows(ValidationException.class, () -> new CategoryCode(null));
    }
}
