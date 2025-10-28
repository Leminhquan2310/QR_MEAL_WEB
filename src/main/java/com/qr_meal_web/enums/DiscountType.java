package com.qr_meal_web.enums;

public enum DiscountType {
    PERCENT("PERCENT",  "Phần trăm (%)"),
    FIXED("FIXED", "Giá tiền (VNĐ)");

    private String value;
    private String label;

    DiscountType(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public static DiscountType fromString(String type) {
        if (type == null) return null;
        try {
            return DiscountType.valueOf(type.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
