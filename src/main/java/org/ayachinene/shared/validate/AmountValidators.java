package org.ayachinene.shared.validate;

import org.ayachinene.utils.BigDecimals;

import java.math.BigDecimal;

public final class AmountValidators {

    private AmountValidators() {
    }

    public static Validator<BigDecimal> positive() {
        return Validators.require(
            value -> BigDecimals.gt(value, BigDecimal.ZERO),
            field -> field + " must be greater than 0"
        );
    }

    public static Validator<BigDecimal> nonNegative() {
        return Validators.require(
            value -> BigDecimals.gte(value, BigDecimal.ZERO),
            field -> field + " must not be negative"
        );
    }

    public static Validator<BigDecimal> range(
        BigDecimal first,
        BigDecimal second
    ) {
        return Validators.require(
            value -> BigDecimals.between(value, first, second),
            field -> field + " must be between " + first.min(second).toPlainString() + " and " + first.max(second).toPlainString());
    }

    public static Validator<BigDecimal> maxFractionDigits(int maximum) {
        return Validators.require(value -> !BigDecimals.hasMoreFractionDigitsThan(value, maximum),
            field -> field + " must have at most " + maximum + " fraction digits");
    }
}
