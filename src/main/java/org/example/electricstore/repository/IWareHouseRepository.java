package org.example.electricstore.repository;

import org.example.electricstore.model.Product;
import org.example.electricstore.model.WareHouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface IWareHouseRepository extends JpaRepository<WareHouse, Integer> {
    @Query("SELECT w FROM WareHouse w " +
            "JOIN w.product p " +
            "JOIN p.supplier s " +
            "WHERE ((:keyword IS NULL OR :keyword = '' OR " +
            "        (:field = 'productName' AND p.name LIKE %:keyword%) " +
            "     OR (:field = 'supplierName' AND s.name LIKE %:keyword%))) " +
            "AND (:status IS NULL OR " +
            "    (:status = 1 AND w.quantity = 0) " +
            "    OR (:status = 3 AND w.quantity >= 100) " +
            "    OR (:status = 2 AND w.quantity < 100))")
    Page<WareHouse> searchWareHouse(@Param("field") String field,
                                    @Param("keyword") String keyword,
                                    @Param("status") Integer status,
                                    Pageable pageable);
    WareHouse findByProduct (Product product);


}
