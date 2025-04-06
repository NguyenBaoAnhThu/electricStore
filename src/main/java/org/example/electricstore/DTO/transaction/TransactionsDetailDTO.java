package org.example.electricstore.DTO.transaction;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionsDetailDTO {

    private Integer id;
    private String productName ;
    private String supplierName ;
    private Integer quantity ;
    private Double price ;
}
