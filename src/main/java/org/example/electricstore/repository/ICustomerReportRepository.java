package org.example.electricstore.repository;

import org.example.electricstore.model.CustomerReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ICustomerReportRepository extends JpaRepository<CustomerReport, Integer> {
    @Query(value = "SELECT c.customer_id, " +
            "c.customer_name, " +
            "COUNT(o.orderID) as total_orders, " +
            "SUM(o.total_price) as total_spent, " +
            "SUM(od.quantity) as total_products, " +
            "MAX(o.create_at) as last_order_date, " +
            "MAX(o.orderID) as last_order_id, " +
            "MAX(p.paymentID) as last_payment_id " +
            "FROM order_products o " +
            "JOIN customers c ON o.customer_id = c.customer_id " +
            "JOIN order_details od ON o.orderID = od.order_id " +
            "JOIN payments p ON o.orderID = p.order_id " +
            "WHERE o.create_at BETWEEN :startDate AND :endDate " +
            "AND o.payment_status = 'COMPLETED' " +
            "GROUP BY c.customer_id, c.customer_name " +
            "ORDER BY MAX(o.create_at) DESC",
            countQuery = "SELECT COUNT(DISTINCT c.customer_id) " +
                    "FROM order_products o " +
                    "JOIN customers c ON o.customer_id = c.customer_id " +
                    "WHERE o.create_at BETWEEN :startDate AND :endDate " +
                    "AND o.payment_status = 'COMPLETED'",
            nativeQuery = true)
    Page<Object[]> getCompletedOrderSummary(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    @Query(value = "SELECT COUNT(DISTINCT c.customer_id) " +
            "FROM order_products o " +
            "JOIN customers c ON o.customer_id = c.customer_id " +
            "WHERE o.create_at BETWEEN :startDate AND :endDate " +
            "AND o.payment_status = 'COMPLETED'",
            nativeQuery = true)
    long countTotalCustomersByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}