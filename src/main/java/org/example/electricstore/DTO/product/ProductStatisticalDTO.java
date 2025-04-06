package org.example.electricstore.DTO.product;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductStatisticalDTO {

    private Integer productId;
    private String productName;
    private String brand;
    private String category;
    private String supplier;
    private Double price;
    private Long stock;
}
