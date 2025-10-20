package com.qr_meal_web.enums;

public enum TableStatus {
    INACTIVE(0, "Ngưng hoạt động", "secondary"),
    AVAILABLE(1, "Trống", "success"),
    OCCUPIED(2, "Đang phục vụ", "warning");

    private int code;
    private String label;
    private String badge;

    TableStatus(int code, String label, String badge) {
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

    public static TableStatus fromCode(int code) {
        for (TableStatus status : TableStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid TableStatus code: " + code);
    }

    public static TableStatus fromOrderStatus(OrderStatus orderStatus) {
        if (orderStatus == null) return AVAILABLE;
        return switch (orderStatus) {
            case PENDING, CONFIRMED, SERVING -> OCCUPIED;
            default -> AVAILABLE;
        };
    }

}