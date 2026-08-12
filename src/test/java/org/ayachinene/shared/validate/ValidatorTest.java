package org.ayachinene.shared.validate;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorTest {

    private static final Validator<String> REQUIRED_TEXT =
        StringValidators.text(5);

    @Test
    void composesValidationAndNormalizationInOrder() {
        assertEquals("apple", REQUIRED_TEXT.v(" apple ", "name"));
    }

    @Test
    void reportsTheFieldThatFailed() {
        var exception = assertThrows(
            ValidationException.class,
            () -> REQUIRED_TEXT.v("      ", "name")
        );

        assertEquals("name must not be blank", exception.getMessage());
    }

    @Test
    void whenPresentSkipsTheWrappedValidatorForNull() {
        var optionalText = Validators.whenPresent(REQUIRED_TEXT);

        assertNull(optionalText.v(null, "subtitle"));
    }

    @Test
    void infersNotNullResultTypeFromItsValue() {
        String value = Validators.notNull("text", "name");

        assertEquals("text", value);
    }

    @Test
    void validatesAndNormalizesEveryListElementWithItsIndex() {
        var texts = ListValidators.each(REQUIRED_TEXT);

        var exception = assertThrows(
            ValidationException.class,
            () -> texts.v(List.of("one", "too long"), "values")
        );

        assertEquals(
            "values[1] must not exceed 5 characters",
            exception.getMessage()
        );
    }

    @Test
    void checksUniquenessAfterElementNormalization() {
        var uniqueTexts = ListValidators.each(REQUIRED_TEXT)
            .c(ListValidators.unique());

        var exception = assertThrows(
            ValidationException.class,
            () -> uniqueTexts.v(List.of("apple", " apple "), "values")
        );

        assertEquals("values[1] is duplicated", exception.getMessage());
    }

    @Test
    void normalizesNullListToEmptyList() {
        var optionalList = ListValidators.<String>nullAsEmpty()
            .c(ListValidators.each(REQUIRED_TEXT));

        assertEquals(List.of(), optionalList.v(null, "values"));
    }

    @Test
    void eachDoesNotImplicitlyRejectNullElements() {
        var optionalTexts = ListValidators.each(
            Validators.whenPresent(REQUIRED_TEXT)
        );

        assertEquals(
            Arrays.asList(null, "one"),
            optionalTexts.v(Arrays.asList(null, " one "), "values")
        );
    }
}
