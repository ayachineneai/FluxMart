package org.ayachinene.utils;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BigDecimalsTest {

    @Test
    void comparesValuesIgnoringScale() {
        assertTrue(BigDecimals.eq(
            new BigDecimal("1.0"),
            new BigDecimal("1.00")
        ));
        assertTrue(BigDecimals.gt(
            new BigDecimal("1.01"),
            new BigDecimal("1.00")
        ));
        assertTrue(BigDecimals.gte(
            new BigDecimal("1.00"),
            new BigDecimal("1.0")
        ));
        assertTrue(BigDecimals.lt(
            new BigDecimal("0.99"),
            BigDecimal.ONE
        ));
        assertTrue(BigDecimals.lte(
            BigDecimal.ONE,
            new BigDecimal("1.00")
        ));
    }

    @Test
    void comparisonsReturnFalseWhenEitherValueIsNull() {
        assertFalse(BigDecimals.eq(null, BigDecimal.ZERO));
        assertFalse(BigDecimals.gt(BigDecimal.ZERO, null));
        assertFalse(BigDecimals.gte(null, null));
        assertFalse(BigDecimals.lt(null, BigDecimal.ZERO));
        assertFalse(BigDecimals.lte(BigDecimal.ZERO, null));
    }

    @Test
    void checksBetweenWithBoundariesInEitherOrder() {
        assertTrue(BigDecimals.between(
            new BigDecimal("5.00"),
            new BigDecimal("10.00"),
            new BigDecimal("1.00")
        ));
        assertTrue(BigDecimals.between(
            new BigDecimal("1.00"),
            new BigDecimal("1.00"),
            new BigDecimal("10.00")
        ));
    }

    @Test
    void betweenReturnsFalseForNull() {
        assertFalse(BigDecimals.between(null, BigDecimal.ZERO, BigDecimal.ONE));
        assertFalse(BigDecimals.between(BigDecimal.ZERO, null, BigDecimal.ONE));
        assertFalse(BigDecimals.between(BigDecimal.ZERO, BigDecimal.ONE, null));
    }
}
