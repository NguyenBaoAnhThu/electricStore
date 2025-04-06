package org.example.electricstore.DTO.order;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailDTO {
    private String productName ;
    private Double productPrice ;
    private Integer productQuantity ;
}
