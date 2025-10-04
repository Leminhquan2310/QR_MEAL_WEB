package com.qr_meal_web.util;

public class Helper {
    public static double parseDoubleSafe(String value, double defaultValue) {
        if (value == null || value.trim().isEmpty()) return defaultValue;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static int parseIntegerSafe(String value, Integer defaultValue) {
        if (value == null || value.trim().isEmpty()) return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
