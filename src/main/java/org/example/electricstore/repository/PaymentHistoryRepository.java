package org.example.electricstore.repository;

import org.example.electricstore.model.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
    @Query("SELECT p FROM PaymentHistory p WHERE p.invoiceId = :invoiceId ORDER BY p.paidAt ASC")
    List<PaymentHistory> findByInvoiceId(@Param("invoiceId") Long invoiceId);
}