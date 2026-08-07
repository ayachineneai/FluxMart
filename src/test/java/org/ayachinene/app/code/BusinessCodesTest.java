package org.ayachinene.app.code;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.ayachinene.app.exception.ValidationException;

class BusinessCodesTest {

    private static final String ALLOWED_CHARACTERS =
            "23456789ABCDEFGHJKMNPQRSTVWXYZ";

    @Test
    void generatesTwentyHumanFriendlyCharacters() {
        var code = BusinessCodes.generateBody();

        assertEquals(20, code.length());
        assertTrue(code.chars().allMatch(character ->
                ALLOWED_CHARACTERS.indexOf(character) >= 0
        ));
    }

    @Test
    void validatesBusinessCodeFormat() {
        assertEquals(
                "PRD_23456789ABCDEFGHJKMN",
                BusinessCodes.validate(
                        "PRD_23456789ABCDEFGHJKMN",
                        "PRD_",
                        "productCode"
                )
        );
        assertThrows(
                ValidationException.class,
                () -> BusinessCodes.validate("INVALID", "PRD_", "productCode")
        );
    }
}
