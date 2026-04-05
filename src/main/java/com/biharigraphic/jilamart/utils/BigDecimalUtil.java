package com.biharigraphic.jilamart.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtil {

    // Default scale for currency (2 decimal places)
    private static final int DEFAULT_SCALE = 2;

    // Safe addition: handles nulls as zero
    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        return (a == null ? BigDecimal.ZERO : a).add(b == null ? BigDecimal.ZERO : b);
    }

    // Safe subtraction: handles nulls as zero
    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        return (a == null ? BigDecimal.ZERO : a).subtract(b == null ? BigDecimal.ZERO : b);
    }

    // Safe multiplication: handles nulls as zero
    public static BigDecimal multiply(BigDecimal a, BigDecimal b) {
        return (a == null ? BigDecimal.ZERO : a).multiply(b == null ? BigDecimal.ZERO : b);
    }

    // Safe division with scale and rounding mode, handles nulls as zero (returns zero if divisor is zero)
    public static BigDecimal divide(BigDecimal a, BigDecimal b) {
        return divide(a, b, DEFAULT_SCALE, RoundingMode.HALF_UP);
    }

    public static BigDecimal divide(BigDecimal a, BigDecimal b, int scale, RoundingMode roundingMode) {
        if (a == null) a = BigDecimal.ZERO;
        if (b == null || BigDecimal.ZERO.compareTo(b) == 0) {
            // Prevent division by zero
            return BigDecimal.ZERO.setScale(scale, roundingMode);
        }
        return a.divide(b, scale, roundingMode);
    }

    // Safe comparison: returns -1 if a < b, 0 if equal, 1 if a > b
    public static int compare(BigDecimal a, BigDecimal b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        return a.compareTo(b);
    }

    // Safe equals check
    public static boolean equals(BigDecimal a, BigDecimal b) {
        return compare(a, b) == 0;
    }

    // Format BigDecimal to string with default scale and no scientific notation
    public static String format(BigDecimal value) {
        if (value == null) return "0.00";
        return value.setScale(DEFAULT_SCALE, RoundingMode.HALF_UP).toPlainString();
    }

    // Format BigDecimal to string with custom scale
    public static String format(BigDecimal value, int scale) {
        if (value == null) return String.format("%." + scale + "f", 0);
        return value.setScale(scale, RoundingMode.HALF_UP).toPlainString();
    }

    // Parse string safely to BigDecimal (returns zero if invalid)
    public static BigDecimal parse(String value) {
        if (value == null || value.trim().isEmpty()) return BigDecimal.ZERO;
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}
