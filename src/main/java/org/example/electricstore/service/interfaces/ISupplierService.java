package org.example.electricstore.service.interfaces;

import org.example.electricstore.model.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ISupplierService {
    Page<Supplier> getSuppliers(int page, int size);

    List<Supplier> getAllSuppliers();

    Optional<Supplier> getSupplierById(Integer id);

    Supplier addSupplier(Supplier supplier);

    Supplier updateSupplier(Integer id, Supplier supplierDetails);

    void deleteSuppliers(List<Integer> ids);

    Page<Supplier> searchSuppliersByAllFields(String supplierCode, String name, String address, String phone, String email, Pageable pageable);

    List<Supplier> searchSuppliersByAllFields(String supplierCode, String name, String address, String phone, String email);
}