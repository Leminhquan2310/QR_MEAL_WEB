package com.qr_meal_web.enums;

public enum BankAccountStatus {
    ACTIVE(1, "Hoạt động", "success"),
    INACTIVE(0, "Ngưng hoạt động", "secondary");

    private int code;
    private String label;
    private String badge;

    BankAccountStatus(int code, String label, String badge) {
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

    public static BankAccountStatus fromCode(int code) {
        for (BankAccountStatus status : BankAccountStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid BankAccountStatus code: " + code);
    }
}
