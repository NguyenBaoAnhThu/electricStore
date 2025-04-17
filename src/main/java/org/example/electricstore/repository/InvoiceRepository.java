package org.example.electricstore.repository;


import org.example.electricstore.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query("SELECT i FROM Invoice i LEFT JOIN FETCH i.products WHERE i.id = :id")
    Optional<Invoice> findByIdWithProducts(@Param("id") Long id);
}