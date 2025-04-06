package org.example.electricstore.service.impl;


import org.example.electricstore.DTO.product.ProductChoiceDTO;
import org.example.electricstore.DTO.product.ProductChoiceRequestDTO;
import org.example.electricstore.DTO.transaction.TransactionsDetailDTO;
import org.example.electricstore.enums.TransactionType;
import org.example.electricstore.mapper.transaction.TransactionMapper;
import org.example.electricstore.model.Employee;
import org.example.electricstore.model.InventoryTransaction;
import org.example.electricstore.model.Product;
import org.example.electricstore.model.WareHouse;
import org.example.electricstore.repository.IEmployeeRepository;
import org.example.electricstore.repository.IInventoryTransactionRepository;
import org.example.electricstore.repository.IProductRepository;
import org.example.electricstore.repository.IWareHouseRepository;
import org.example.electricstore.service.interfaces.IInventoryTransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class InventoryTransactionService implements IInventoryTransactionService
        <InventoryTransaction, ProductChoiceRequestDTO , TransactionsDetailDTO> {

    private final TransactionMapper transactionMapper;
    private final IWareHouseRepository wareHouseRepository;
    private final IInventoryTransactionRepository inventoryTransactionRepository;
    private final IEmployeeRepository employeeRepository;
    private final IProductRepository productRepository;

    public InventoryTransactionService(TransactionMapper transactionMapper,
                                       IWareHouseRepository wareHouseRepository,
                                       IInventoryTransactionRepository inventoryTransactionRepository,
                                       IEmployeeRepository employeeRepository,
                                       IProductRepository productRepository) {
        this.transactionMapper = transactionMapper;
        this.wareHouseRepository = wareHouseRepository;
        this.inventoryTransactionRepository = inventoryTransactionRepository;
        this.employeeRepository = employeeRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void importToWarehouse(ProductChoiceRequestDTO productChoiceRequestDTO) {
        List<ProductChoiceDTO> productChoiceDTOList = productChoiceRequestDTO.getProducts();

        for (ProductChoiceDTO productChoiceDTO : productChoiceDTOList) {
            Product product = this.productRepository.findById(productChoiceDTO.getProductId()).orElse(null);
            WareHouse wareHouse = this.wareHouseRepository.findByProduct(product);
            if (wareHouse != null) {
                wareHouse.setQuantity(wareHouse.getQuantity() + productChoiceDTO.getProductQuantity());
                wareHouse.setPrice(productChoiceDTO.getProductPrice());
                this.wareHouseRepository.save(wareHouse);
            } else {
                this.wareHouseRepository.save(WareHouse.builder()
                        .product(product)
                        .quantity(productChoiceDTO.getProductQuantity())
                        .price(productChoiceDTO.getProductPrice())
                        .build());
            }
        }
        InventoryTransaction inventoryTransaction = this.transactionMapper.convertToInventoryTransactionImport(productChoiceRequestDTO);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee employee = this.employeeRepository.findEmployeeByUser_Username(username);
        if (employee != null) {
            inventoryTransaction.setEmployee(employee);
        }
        this.inventoryTransactionRepository.save(inventoryTransaction);
    }

    @Override
    public void exportFromWarehouse(ProductChoiceRequestDTO productChoiceRequestDTO) {
        List<ProductChoiceDTO> productChoiceDTOList = productChoiceRequestDTO.getProducts();
        for (ProductChoiceDTO productChoiceDTO : productChoiceDTOList) {
            Product product = this.productRepository.findById(productChoiceDTO.getProductId()).orElse(null);
            WareHouse wareHouse = this.wareHouseRepository.findByProduct(product);
            wareHouse.setQuantity(wareHouse.getQuantity() - productChoiceDTO.getQuantityInput());
            this.wareHouseRepository.save(wareHouse);
        }
        InventoryTransaction inventoryTransaction = this.transactionMapper.convertToInventoryTransactionExport(productChoiceRequestDTO);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Employee employee = this.employeeRepository.findEmployeeByUser_Username(username);
        if (employee != null) {
            inventoryTransaction.setEmployee(employee);
        }
        this.inventoryTransactionRepository.save(inventoryTransaction);
    }
    @Override
    public Page<InventoryTransaction> searchTransactions(String field, String keyword, int page, int size,
                                                         LocalDate fromDate, LocalDate toDate, String transactionType) {
        Pageable pageable1 = PageRequest.of(page - 1, size);
        if (transactionType != null && !transactionType.isEmpty()) {
            return this.inventoryTransactionRepository.searchTransactions(field , keyword,
                    TransactionType.valueOf(transactionType) , fromDate, toDate, pageable1);
        }
        return this.inventoryTransactionRepository.searchTransactions(field , keyword ,
                null , fromDate, toDate, pageable1);
    }

    @Override
    public List<TransactionsDetailDTO> getTransactionsDetail(Integer id) {
        InventoryTransaction inventoryTransaction = this.inventoryTransactionRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Inventory transaction not found"));
        return this.transactionMapper.convertToTransactionsDetailDTO(inventoryTransaction) ;
    }



}
