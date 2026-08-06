package org.ayachinene.utils;

import java.math.BigDecimal;
import java.util.Objects;

public final class BigDecimals {

    private BigDecimals() {
    }

    public static boolean isNegative(BigDecimal value) {
        Objects.requireNonNull(value, "value must not be null");
        return value.signum() < 0;
    }

    public static boolean isPositive(BigDecimal value) {
        Objects.requireNonNull(value, "value must not be null");
        return value.signum() > 0;
    }

    public static boolean isGreaterThan(BigDecimal value, BigDecimal other) {
        Objects.requireNonNull(value, "value must not be null");
        Objects.requireNonNull(other, "other must not be null");
        return value.compareTo(other) > 0;
    }

    public static boolean hasMoreFractionDigitsThan(BigDecimal value, int maximum) {
        Objects.requireNonNull(value, "value must not be null");
        return value.stripTrailingZeros().scale() > maximum;
    }
}
