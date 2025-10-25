package com.qr_meal_web.enums;

public enum OrderStatus {
    PENDING(0, "Chờ xác nhận", "secondary"),
    CONFIRMED(1, "Đã xác nhận", "info"),
    SERVING(2, "Đang phục vụ", "warning text-dark"),
    DONE(3, "Hoàn thành", "success"),
    CANCELLED(4, "Đã hủy", "danger");

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
