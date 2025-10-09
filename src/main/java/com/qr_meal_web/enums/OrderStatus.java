package com.qr_meal_web.enums;

public enum OrderStatus {
    PENDING(0, "Pending", "secondary"),
    CONFIRMED(1, "Confirmed", "info"),
    SERVING(2, "Serving", "warning text-dark"),
    DONE(3, "Done", "success"),
    CANCELLED(4, "Cancelled", "danger");

    private final int code;
    private final String label;
    private final String badge;

    OrderStatus(int code, String label, String badge) {
        this.code = code;
        this.label = label;
        this.badge = badge;
    }

    public String getBadge() {
        return badge;
    }

    public int getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static OrderStatus fromCode(int code) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.code == code) return status;
        }
        throw new IllegalArgumentException("Invalid OrderStatus code: " + code);
    }
}
