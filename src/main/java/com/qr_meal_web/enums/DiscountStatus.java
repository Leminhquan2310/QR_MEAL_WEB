package com.qr_meal_web.enums;

public enum DiscountStatus {
    INACTIVE(0, "Ngưng hoạt động", "secondary"),
    ACTIVE(1, "Hoạt động", "success");

    private int code;
    private String label;
    private String badge;

    DiscountStatus(int code, String label, String badge) {
        this.code = code;
        this.label = label;
        this.badge = badge;
    }

    public int getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public String getBadge() {
        return badge;
    }

    public static DiscountStatus fromCode(int code) {
        for (DiscountStatus status : DiscountStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid DiscountStatus code: " + code);
    }
}
