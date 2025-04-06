package org.example.electricstore.mapper.transaction;


import org.example.electricstore.DTO.product.ProductChoiceRequestDTO;
import org.example.electricstore.DTO.transaction.TransactionsDetailDTO;
import org.example.electricstore.enums.TransactionType;
import org.example.electricstore.model.InventoryTransaction;
import org.example.electricstore.model.TransactionDetail;
import org.example.electricstore.repository.IProductRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionMapper {
    private final IProductRepository productRepository;
    public TransactionMapper(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public InventoryTransaction convertToInventoryTransactionImport(ProductChoiceRequestDTO productChoiceRequestDTO) {
        InventoryTransaction inventoryTransaction = InventoryTransaction.builder()
                .transactionType(TransactionType.IMPORT).build();

        List<TransactionDetail> transactionDetailList = productChoiceRequestDTO.getProducts().stream().map(
                productChoiceDTO -> TransactionDetail.builder()
                       .product(this.productRepository.findById(productChoiceDTO.getProductId()).orElse(null))
                       .quantity(productChoiceDTO.getProductQuantity())
                       .price(productChoiceDTO.getProductPrice())
                       .inventoryTransaction(inventoryTransaction)
                       .build()
        ).toList();

        inventoryTransaction.setTransactionDetails(transactionDetailList);
        return inventoryTransaction;
    }
    public InventoryTransaction convertToInventoryTransactionExport(ProductChoiceRequestDTO productChoiceRequestDTO) {
        InventoryTransaction inventoryTransaction = InventoryTransaction.builder()
                .transactionType(TransactionType.EXPORT)
                .build();

        List<TransactionDetail> transactionDetailList = productChoiceRequestDTO.getProducts().stream().map(
                productChoiceDTO -> TransactionDetail.builder()
                        .product(this.productRepository.findById(productChoiceDTO.getProductId()).orElse(null))
                        .quantity(productChoiceDTO.getQuantityInput())
                        .inventoryTransaction(inventoryTransaction)
                        .price(productChoiceDTO.getProductPrice())
                        .build()
        ).toList();

        inventoryTransaction.setTransactionDetails(transactionDetailList);
        return inventoryTransaction;
    }

    public List<TransactionsDetailDTO> convertToTransactionsDetailDTO(InventoryTransaction inventoryTransaction) {
       return inventoryTransaction.getTransactionDetails().stream().map(transactionDetail ->
               TransactionsDetailDTO.builder()
                       .id(transactionDetail.getId())
                       .productName(transactionDetail.getProduct().getName())
                       .supplierName(transactionDetail.getProduct().getSupplier().getName())
                       .quantity(transactionDetail.getQuantity())
                       .price(transactionDetail.getPrice())
                       .build()).toList() ;
    }

}