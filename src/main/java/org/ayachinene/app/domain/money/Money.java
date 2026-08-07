package org.ayachinene.app.domain.money;

import org.ayachinene.utils.BigDecimals;
import org.ayachinene.utils.Validates;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money(BigDecimal amount) {

    public static final BigDecimal MAX_AMOUNT = new BigDecimal("99999999.99");

    private static final int FRACTION_DIGITS = 2;

    public Money {
        amount = validate(amount);
    }

    public static BigDecimal validate(BigDecimal amount) {
        Validates.requireNonNull(amount, "amount");
        Validates.require(!BigDecimals.isNegative(amount), "amount must not be negative");
        Validates.require(
                !BigDecimals.hasMoreFractionDigitsThan(amount, FRACTION_DIGITS),
                "amount has too many fraction digits"
        );
        Validates.require(
                !BigDecimals.isGreaterThan(amount, MAX_AMOUNT),
                "amount must not exceed " + MAX_AMOUNT
        );
        return amount.setScale(FRACTION_DIGITS, RoundingMode.UNNECESSARY);
    }

}
