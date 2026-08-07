package org.ayachinene.app.product;

import org.ayachinene.app.product.domain.SkuSpecificationValidator;
import org.ayachinene.app.product.creation.SelectionInput;
import org.ayachinene.app.product.creation.SkuInput;
import org.ayachinene.app.product.creation.SpecificationInput;
import org.ayachinene.app.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SkuSpecificationValidatorTest {

    private static final List<SpecificationInput> SPECIFICATIONS = List.of(
            new SpecificationInput("颜色", List.of("黑色", "白色")),
            new SpecificationInput("尺码", List.of("M", "L"))
    );

    @Test
    void acceptsACompleteSkuConfiguration() {
        assertDoesNotThrow(() -> SkuSpecificationValidator.validate(
                SPECIFICATIONS,
                List.of(sku(
                        new SelectionInput("颜色", "黑色"),
                        new SelectionInput("尺码", "M")
                ))
        ));
    }

    @Test
    void acceptsOneDefaultSkuForProductWithoutSpecifications() {
        assertDoesNotThrow(() -> SkuSpecificationValidator.validate(
                List.of(),
                List.of(sku())
        ));
    }

    @Test
    void rejectsMultipleSkusForProductWithoutSpecifications() {
        assertThrows(ValidationException.class, () -> SkuSpecificationValidator.validate(
                List.of(),
                List.of(sku(), sku())
        ));
    }

    @Test
    void rejectsSelectionsForProductWithoutSpecifications() {
        assertThrows(ValidationException.class, () -> SkuSpecificationValidator.validate(
                List.of(),
                List.of(sku(new SelectionInput("颜色", "黑色")))
        ));
    }

    @Test
    void rejectsAnIncompleteSkuConfiguration() {
        assertThrows(ValidationException.class, () -> SkuSpecificationValidator.validate(
                SPECIFICATIONS,
                List.of(sku(new SelectionInput("颜色", "黑色")))
        ));
    }

    @Test
    void rejectsAValueOutsideItsSpecification() {
        assertThrows(ValidationException.class, () -> SkuSpecificationValidator.validate(
                SPECIFICATIONS,
                List.of(sku(
                        new SelectionInput("颜色", "M"),
                        new SelectionInput("尺码", "L")
                ))
        ));
    }

    private static SkuInput sku(SelectionInput... selections) {
        return new SkuInput(
                null,
                BigDecimal.ONE,
                null,
                List.of(selections)
        );
    }
}
