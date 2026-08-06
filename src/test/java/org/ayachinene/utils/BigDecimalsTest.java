package org.ayachinene.utils;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BigDecimalsTest {

    @Test
    void identifiesNegativeValues() {
        assertTrue(BigDecimals.isNegative(new BigDecimal("-0.01")));
        assertFalse(BigDecimals.isNegative(BigDecimal.ZERO));
        assertFalse(BigDecimals.isNegative(new BigDecimal("0.01")));
    }

    @Test
    void identifiesPositiveValues() {
        assertTrue(BigDecimals.isPositive(new BigDecimal("0.01")));
        assertFalse(BigDecimals.isPositive(BigDecimal.ZERO));
        assertFalse(BigDecimals.isPositive(new BigDecimal("-0.01")));
    }

    @Test
    void identifiesMeaningfulFractionDigits() {
        assertTrue(BigDecimals.hasMoreFractionDigitsThan(new BigDecimal("1.001"), 2));
        assertFalse(BigDecimals.hasMoreFractionDigitsThan(new BigDecimal("1.000"), 2));
    }
}
