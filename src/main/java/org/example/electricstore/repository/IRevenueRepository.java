package org.example.electricstore.repository;

import org.example.electricstore.DTO.product.ProductStatisticalDTO;
import org.example.electricstore.DTO.statistical.TopSellingProductDTO;
import org.example.electricstore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Transactional
public interface IRevenueRepository extends JpaRepository <Order , Integer> {

        @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderDetails " +
                "WHERE o.createAt >= :startDate AND o.createAt < :endDate")
        List<Order> findAllByOrderByCreateAt(
                @Param("startDate") LocalDateTime startDate,
                @Param("endDate") LocalDateTime endDate
        );
        @Query("SELECT new org.example.electricstore.DTO.statistical.TopSellingProductDTO" +
                "(od.product.productID, od.product.name, SUM(od.quantity), SUM(od.price)) " +
                "FROM OrderDetail od " +
                "WHERE od.order.orderID IN :orderIds " +
                "GROUP BY od.product.productID, od.product.name " +
                "ORDER BY SUM(od.quantity) DESC LIMIT 10")
        List<TopSellingProductDTO> findTopSellingProducts(
                @Param("orderIds") List<Integer> orderIds
        );
        @Query("SELECT new org.example.electricstore.DTO.product.ProductStatisticalDTO( " +
                "p.productID, p.name, b.name, c.name, s.name, p.price, SUM(od.quantity)) " +
                "FROM OrderDetail od " +
                "JOIN od.order o " +
                "JOIN od.product p " +
                "JOIN p.brand b " +
                "JOIN p.category c " +
                "JOIN p.supplier s " +
                "WHERE o.orderID IN :orderIds " +
                "GROUP BY p.productID, p.name, b.name, c.name, s.name, p.price" +
                " ORDER BY SUM(od.quantity) DESC")
        List<ProductStatisticalDTO> findProductsSales(@Param("orderIds") List<Integer> orderIds);

        @Query("SELECT o FROM Order o ORDER BY o.createAt ASC")
        List<Order> findAllOrders();
}
