package org.example.electricstore.repository;

import org.example.electricstore.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    @Query(value = "insert into order_detail (order_id, product_id, quantity, price) values (?1, ?2, ?3, ?4)", nativeQuery = true)
    void insertOrderDetail(Integer orderId, Integer productId, Integer quantity, Integer price);

}
