package org.example.electricstore.repository;

import org.example.electricstore.enums.TransactionType;
import org.example.electricstore.model.InventoryTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface IInventoryTransactionRepository extends JpaRepository<InventoryTransaction, Integer> {

    @Query("""
    SELECT DISTINCT it FROM InventoryTransaction it
    LEFT JOIN it.transactionDetails td
    LEFT JOIN td.product p
    LEFT JOIN p.supplier s
    LEFT JOIN it.employee e
    WHERE (
        (:field = 'productName' AND LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
        OR (:field = 'supplierName' AND LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
        OR (:field = 'employeeName' AND LOWER(e.employeeName) LIKE LOWER(CONCAT('%', :keyword, '%')))
        OR (:keyword = '' OR :field NOT IN ('productName', 'supplierName', 'employeeName'))
    )
    AND (:transactionType IS NULL OR it.transactionType = :transactionType)
    AND (:fromDate IS NULL OR it.createdAt >= :fromDate)
    AND (:toDate IS NULL OR it.createdAt <= :toDate)
    ORDER BY it.createdAt DESC , it.id DESC
""")
    Page<InventoryTransaction> searchTransactions(
            @Param("field") String field,
            @Param("keyword") String keyword,
            @Param("transactionType") TransactionType transactionType,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            Pageable pageable
    );



}
