package com.qr_meal_web.enums;

public enum PaymentMethod {
    CASH("cash", "Tiền mặt"),
    BANK("bank", "Chuyển khoản");

    private final String code;
    private final String label;

    PaymentMethod(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static PaymentMethod fromCode(String code) {
        for (PaymentMethod status : PaymentMethod.values()) {
            if (status.code.equals(code)) return status;
        }
        throw new IllegalArgumentException("Invalid PaymentMethod code: " + code);
    }
}
