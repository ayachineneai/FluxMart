package org.ayachinene.app.product;

import org.ayachinene.app.domain.product.specification.SpecificationValidator;
import org.ayachinene.app.domain.product.creation.SpecificationInput;
import org.ayachinene.app.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SpecificationValidatorTest {

    @Test
    void normalizesSpecificationNamesAndValues() {
        var result = SpecificationValidator.validate(List.of(
                new SpecificationInput(" 颜色 ", List.of(" 黑色 ", "白色"))
        ));

        assertEquals(
                List.of(new SpecificationInput("颜色", List.of("黑色", "白色"))),
                result
        );
    }

    @Test
    void rejectsDuplicatedSpecificationsAndValues() {
        assertThrows(ValidationException.class, () -> SpecificationValidator.validate(List.of(
                new SpecificationInput("颜色", List.of("黑色")),
                new SpecificationInput(" 颜色 ", List.of("白色"))
        )));
        assertThrows(ValidationException.class, () -> SpecificationValidator.validate(List.of(
                new SpecificationInput("颜色", List.of("黑色", " 黑色 "))
        )));
    }

    @Test
    void rejectsASpecificationWithoutValues() {
        assertThrows(ValidationException.class, () -> SpecificationValidator.validate(List.of(
                new SpecificationInput("颜色", List.of())
        )));
        assertThrows(ValidationException.class, () -> SpecificationValidator.validate(List.of(
                new SpecificationInput("颜色", null)
        )));
    }
}
