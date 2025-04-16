package org.example.electricstore.repository;

import org.example.electricstore.model.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {

    @Query("SELECT i FROM InvoiceItem i JOIN FETCH i.invoice")
    List<InvoiceItem> findAllWithInvoice();


}
