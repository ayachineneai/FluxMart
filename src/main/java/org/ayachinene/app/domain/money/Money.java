package org.ayachinene.app.domain.money;

import org.ayachinene.utils.BigDecimals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal amount) {

    public static final BigDecimal MAX_AMOUNT = new BigDecimal("99999999.99");

    private static final int FRACTION_DIGITS = 2;

    public Money {
        amount = validate(amount);
    }

    public static BigDecimal validate(BigDecimal amount) {
        Objects.requireNonNull(amount, "amount must not be null");
        if (BigDecimals.isNegative(amount)) {
            throw new IllegalArgumentException("amount must not be negative");
        }
        if (BigDecimals.hasMoreFractionDigitsThan(amount, FRACTION_DIGITS)) {
            throw new IllegalArgumentException("amount has too many fraction digits");
        }
        if (BigDecimals.isGreaterThan(amount, MAX_AMOUNT)) {
            throw new IllegalArgumentException("amount must not exceed " + MAX_AMOUNT);
        }
        return amount.setScale(FRACTION_DIGITS, RoundingMode.UNNECESSARY);
    }

}
