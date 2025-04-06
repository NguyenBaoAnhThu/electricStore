package org.example.electricstore.DTO.product;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductChoiceDTO {

    private Integer productId;
    private String productName;
    private String supplierName;
    private Integer productQuantity ;
    private Integer quantityInput ;
    private Double productPrice;


}
