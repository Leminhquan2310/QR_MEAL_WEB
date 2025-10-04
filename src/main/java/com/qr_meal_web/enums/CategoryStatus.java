package com.qr_meal_web.enums;

public enum CategoryStatus {
    ACTIVE(1, "Hoạt động", "success"),
    INACTIVE(0, "Ngưng hoạt động", "secondary");

    private int code;
    private String label;
    private String badge;

    CategoryStatus(int code, String label, String badge) {
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

    public static CategoryStatus fromCode(int code) {
        for (CategoryStatus status : CategoryStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid CategoryStatus code: " + code);
    }
}
