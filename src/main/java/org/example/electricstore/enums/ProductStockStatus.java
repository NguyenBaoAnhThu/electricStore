package org.example.electricstore.enums;

public enum ProductStockStatus {
    IN_STOCK("Còn hàng"),
    OUT_OF_STOCK("Hết hàng"),
    LOW_STOCK("Sắp hết");

    private final String displayName;

    ProductStockStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ProductStockStatus fromString(String text) {
        for (ProductStockStatus status : ProductStockStatus.values()) {
            if (status.displayName.equalsIgnoreCase(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy trạng thái hợp lệ: " + text);
    }
}

