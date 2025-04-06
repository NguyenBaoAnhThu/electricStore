package org.example.electricstore.model;


import org.example.electricstore.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "inventory_transactions")
public class InventoryTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String transactionCode;
    @OneToMany(mappedBy = "inventoryTransaction", cascade = CascadeType.ALL,
            orphanRemoval = true ,
            fetch = FetchType.EAGER)
    private List<TransactionDetail> transactionDetails;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private LocalDate createdAt;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    private Employee employee;
    
    @PrePersist
    protected void onCreate() {
        String prefix = this.transactionType == TransactionType.IMPORT ? "IMPORT" : "EXPORT";
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uniqueId = UUID.randomUUID().toString().substring(0, 5).toUpperCase();

        this.transactionCode = prefix + "-" + datePart + "-" + uniqueId;
        this.createdAt = LocalDate.now();

    }
}
