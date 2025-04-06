package org.example.electricstore.model;


import org.example.electricstore.enums.PaymentStatus;
import org.example.electricstore.enums.SpendingCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "customer_reports")
public class CustomerReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reportId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer; // Đúng quan hệ 1-1

    @ManyToOne
    @JoinColumn(name = "payment_id")  // Liên kết với bảng Payment
    private Payment lastPayment;

    private Integer totalOrders;  // Tổng số đơn hàng

    private Double totalSpent;  // Tổng số tiền đã chi tiêu

    private Integer totalProductsPurchased;  // Tổng số lượng sản phẩm đã mua

    @Enumerated(EnumType.STRING)
    private PaymentStatus lastPaymentStatus;  // Trạng thái thanh toán của đơn hàng gần nhất

    private LocalDateTime lastOrderDate;  // Ngày đặt hàng gần nhất


    @ManyToOne
    @JoinColumn(name = "last_order_id")
    private Order lastOrder;  // Đơn hàng gần nhất của khách hàng

    @Enumerated(EnumType.STRING)
    private SpendingCategory spendingCategory; // Phân loại khách hàng theo chi tiêu

    public CustomerReport(Customer customer) {

        this.customer = customer;
    }
    public CustomerReport(Customer customer, LocalDateTime startOfDay) {

    }
}
