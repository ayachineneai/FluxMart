package org.ayachinene.utils;

import java.math.BigDecimal;

public final class BigDecimals {

    private BigDecimals() {
    }

    public static boolean isNegative(BigDecimal value) {
        return lt(value, BigDecimal.ZERO);
    }

    public static boolean isPositive(BigDecimal value) {
        return gt(value, BigDecimal.ZERO);
    }

    public static boolean eq(BigDecimal value, BigDecimal other) {
        if (Values.anyNull(value, other)) return false;
        return value.compareTo(other) == 0;
    }

    public static boolean gt(BigDecimal value, BigDecimal other) {
        if (Values.anyNull(value, other)) return false;
        return value.compareTo(other) > 0;
    }

    public static boolean gte(BigDecimal value, BigDecimal other) {
        return gt(value, other) || eq(value, other);
    }

    public static boolean lt(BigDecimal value, BigDecimal other) {
        if (Values.anyNull(value, other)) return false;
        return value.compareTo(other) < 0;
    }

    public static boolean lte(BigDecimal value, BigDecimal other) {
        return lt(value, other) || eq(value, other);
    }

    public static boolean between(
        BigDecimal value,
        BigDecimal first,
        BigDecimal second
    ) {
        if (Values.anyNull(value, first, second)) return false;
        var minimum = first.min(second);
        var maximum = first.max(second);
        return gte(value, minimum) && lte(value, maximum);
    }

    public static boolean isGreaterThan(BigDecimal value, BigDecimal other) {
        return gt(value, other);
    }

    public static boolean hasMoreFractionDigitsThan(BigDecimal value, int maximum) {
        if (value == null) return false;
        return value.stripTrailingZeros().scale() > maximum;
    }

}
