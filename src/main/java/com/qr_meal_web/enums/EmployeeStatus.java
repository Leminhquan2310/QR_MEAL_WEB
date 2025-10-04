package com.qr_meal_web.enums;

public enum EmployeeStatus {
    INACTIVE(0, "Ngưng hoạt động", "secondary"),
    ACTIVE(1, "Hoạt động", "success");

    private int code;
    private String label;
    private String badge;

    EmployeeStatus(int code, String label, String badge) {
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

    public static EmployeeStatus fromCode(int code) {
        for (EmployeeStatus status : EmployeeStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid EmployeeStatus code: " + code);
    }
}
