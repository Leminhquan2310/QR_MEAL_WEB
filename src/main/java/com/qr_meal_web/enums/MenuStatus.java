package com.qr_meal_web.enums;

public enum MenuStatus {
    INACTIVE(0, "Ngưng hoạt động", "secondary"),
    ACTIVE(1, "Hoạt động", "success");

    private int code;
    private String label;
    private String badge;

    MenuStatus(int code, String label, String badge) {
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

    public static MenuStatus fromCode(int code) {
        for (MenuStatus status : MenuStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid MenuStatus code: " + code);
    }
}
