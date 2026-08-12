package org.ayachinene.shared.validate;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AmountValidatorsTest {

    @Test
    void validatesPositiveAmount() {
        assertEquals(
            new BigDecimal("0.01"),
            AmountValidators.positive().v(new BigDecimal("0.01"), "price")
        );
        assertThrows(
            ValidationException.class,
            () -> AmountValidators.positive().v(BigDecimal.ZERO, "price")
        );
    }

    @Test
    void validatesNonNegativeAmount() {
        assertEquals(
            BigDecimal.ZERO,
            AmountValidators.nonNegative().v(BigDecimal.ZERO, "amount")
        );
        assertThrows(
            ValidationException.class,
            () -> AmountValidators.nonNegative().v(
                new BigDecimal("-0.01"),
                "amount"
            )
        );
    }

    @Test
    void includesBothRangeBoundaries() {
        var validator = AmountValidators.range(
            new BigDecimal("10.00"),
            new BigDecimal("1.00")
        );

        assertEquals(
            new BigDecimal("1.00"),
            validator.v(new BigDecimal("1.00"), "amount")
        );
        assertEquals(
            new BigDecimal("10.00"),
            validator.v(new BigDecimal("10.00"), "amount")
        );
    }

    @Test
    void validatesMaximumFractionDigitsIgnoringTrailingZeros() {
        var validator = AmountValidators.maxFractionDigits(2);

        assertEquals(
            new BigDecimal("1.000"),
            validator.v(new BigDecimal("1.000"), "amount")
        );
        assertThrows(
            ValidationException.class,
            () -> validator.v(new BigDecimal("1.001"), "amount")
        );
    }
}
