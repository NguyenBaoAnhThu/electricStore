package org.example.electricstore.repository;

import org.example.electricstore.model.Product;
import org.example.electricstore.model.WareHouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface IWareHouseRepository extends JpaRepository<WareHouse, Integer> {
    @Query("""
    SELECT w FROM WareHouse w
    WHERE (:importDate IS NULL OR w.importDate = :importDate)
      AND (:brand IS NULL OR :brand = '' OR w.product.brand.name = :brand)
      AND (:statusStock IS NULL OR :statusStock = 0 OR 
           (:statusStock = 1 AND w.product.stock = 0) OR 
           (:statusStock = 2 AND w.product.stock > 0 AND w.product.stock <= 100) OR 
           (:statusStock = 3 AND w.product.stock > 100))
      AND (:productCode IS NULL OR :productCode = '' OR w.product.productCode LIKE %:productCode%)
      AND (:productName IS NULL OR :productName = '' OR w.product.name LIKE %:productName%)
""")
    Page<WareHouse> findAllWithFilters(@Param("importDate") LocalDate importDate,
                                       @Param("brand") String brand,
                                       @Param("statusStock") Integer statusStock,
                                       @Param("productCode") String productCode,
                                       @Param("productName") String productName,
                                       Pageable pageable);


    WareHouse findByProduct (Product product);


    @Query("SELECT w FROM WareHouse w WHERE w.product.productID = :productId")
    List<WareHouse> findByProductIdOrderByImportDateDesc(@Param("productId") Integer productId);

}
