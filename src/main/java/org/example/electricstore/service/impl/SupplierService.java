package org.example.electricstore.service.impl;

import org.example.electricstore.model.Supplier;
import org.example.electricstore.repository.ISupplierRepository;
import org.example.electricstore.service.interfaces.ISupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService implements ISupplierService {

    @Autowired
    private ISupplierRepository supplierRepository;

    @Override
    public Page<Supplier> getSuppliers(int page, int size) {
        return supplierRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    @Override
    public Optional<Supplier> getSupplierById(Integer id) {
        return supplierRepository.findById(id);
    }

    @Override
    public Supplier addSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    @Override
    public Supplier updateSupplier(Integer id, Supplier supplierDetails) {
        return supplierRepository.findById(id)
                .map(supplier -> {
                    supplier.setName(supplierDetails.getName());
                    supplier.setAddress(supplierDetails.getAddress());
                    supplier.setPhone(supplierDetails.getPhone());
                    supplier.setEmail(supplierDetails.getEmail());
                    supplier.setSupplierCode(supplierDetails.getSupplierCode());
                    return supplierRepository.save(supplier);
                }).orElseThrow(() -> new RuntimeException("Supplier not found"));
    }

    @Override
    @Transactional
    public void deleteSuppliers(List<Integer> ids) {
        supplierRepository.deleteByIdIn(ids);
    }



    @Override
    public List<Supplier> searchSuppliersByAllFields(String supplierCode, String name, String address, String phone, String email) {
        return List.of();
    }

    @Override
    public Page<Supplier> searchSuppliersByAllFields(
            String supplierCode, String name, String address, String phone, String email, Pageable pageable) {
        return supplierRepository.searchByFields(supplierCode, name, address, phone, email, pageable);
    }
}