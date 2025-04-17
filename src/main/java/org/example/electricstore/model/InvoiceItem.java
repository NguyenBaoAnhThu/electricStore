package org.example.electricstore.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productId;
    private String productCode;
    private String productName;
    private String brand;
    private long quantity;
    private long price;

    @Column(name = "payment_status", nullable = false)
    private String paymentStatus = "CHỜ THANH TOÁN";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;
}
