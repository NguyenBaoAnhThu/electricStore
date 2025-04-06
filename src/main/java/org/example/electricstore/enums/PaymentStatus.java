package org.example.electricstore.enums;

public enum PaymentStatus {
    PENDING("Đang chờ xử lý"),
    COMPLETED("Hoàn tất"),
    FAILED("Thất bại");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static PaymentStatus fromString(String text) {
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.name().equalsIgnoreCase(text) || status.description.equalsIgnoreCase(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy trạng thái thanh toán: " + text);
    }
}
