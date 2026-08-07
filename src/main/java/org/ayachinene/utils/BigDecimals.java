package org.ayachinene.utils;

import java.math.BigDecimal;

public final class BigDecimals {

    private BigDecimals() {
    }

    public static boolean isNegative(BigDecimal value) {
        if (value == null) return false;
        return value.signum() < 0;
    }

    public static boolean isPositive(BigDecimal value) {
        if (value == null) return false;
        return value.signum() > 0;
    }

    public static boolean isGreaterThan(BigDecimal value, BigDecimal other) {
        if (Values.anyNull(value, other)) return false;
        return value.compareTo(other) > 0;
    }

    public static boolean hasMoreFractionDigitsThan(BigDecimal value, int maximum) {
        if (value == null) return false;
        return value.stripTrailingZeros().scale() > maximum;
    }
}
