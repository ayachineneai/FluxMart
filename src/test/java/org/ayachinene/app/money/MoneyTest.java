package org.ayachinene.app.money;

import org.ayachinene.app.domain.money.Money;
import org.ayachinene.app.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyTest {

    @Test
    void rejectsNullAmount() {
        assertThrows(ValidationException.class, () -> new Money(null));
    }

    @Test
    void rejectsNegativeAmount() {
        assertThrows(
                ValidationException.class,
                () -> new Money(new BigDecimal("-0.01"))
        );
    }

    @Test
    void rejectsUnsupportedFractionDigits() {
        assertThrows(
                ValidationException.class,
                () -> new Money(new BigDecimal("1.001"))
        );
    }

    @Test
    void acceptsMaximumAmountAndRejectsLargerAmount() {
        assertEquals(Money.MAX_AMOUNT, new Money(Money.MAX_AMOUNT).amount());
        assertThrows(
                ValidationException.class,
                () -> new Money(new BigDecimal("100000000.00"))
        );
    }

    @Test
    void normalizesAmountToTwoFractionDigits() {
        assertEquals(new BigDecimal("1.00"), new Money(BigDecimal.ONE).amount());
        assertEquals(new BigDecimal("1.00"), new Money(new BigDecimal("1.000")).amount());
        assertEquals(new BigDecimal("1.00"), Money.validate(BigDecimal.ONE));
    }
}
