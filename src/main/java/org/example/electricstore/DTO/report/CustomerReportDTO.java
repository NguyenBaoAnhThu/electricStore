package org.example.electricstore.DTO.report;
import org.example.electricstore.enums.PaymentStatus;
import org.example.electricstore.enums.SpendingCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerReportDTO {
    private Integer customerId;
    private String customerName;
    private int totalOrders;
    private double totalSpent;
    private int totalProductsPurchased;
    private LocalDateTime lastOrderDate;
    private Integer lastOrderId;
    private Integer lastPaymentId;      // Thêm từ logic ban đầu
    private SpendingCategory spendingCategory;
    private PaymentStatus lastPaymentStatus;
}