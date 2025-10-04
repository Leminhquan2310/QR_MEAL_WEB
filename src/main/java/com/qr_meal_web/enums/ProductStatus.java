package com.qr_meal_web.enums;

public enum ProductStatus {
    INACTIVE(0, "Ngừng hoạt động", "secondary"),
    ACTIVE(1, "Hoạt động", "success"),
    OUT_OF_STOCK(2, "Hết hàng", "danger"),
    ARCHIVED(3, "Ngừng kinh doanh, lưu trữ", "dark"),
    DRAFT(4, "Chưa công bố", "warning text-dark");

    private final int code;
    private final String description;
    private final String badge;

    ProductStatus(int code, String description, String badge) {
        this.code = code;
        this.description = description;
        this.badge = badge;
    }

    public String getBadge() {
        return badge;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ProductStatus fromCode(int code) {
        for (ProductStatus productStatus : ProductStatus.values()) {
            if (productStatus.code == code) {
                return productStatus;
            }
        }
        throw new IllegalArgumentException("Invalid ProductStatus code: " + code);
    }
}
