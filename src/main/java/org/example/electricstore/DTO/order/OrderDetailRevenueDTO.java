package org.example.electricstore.DTO.order;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailRevenueDTO {

    private String code;
    private String customerName;
    private LocalDateTime orderDate;
    private Integer quantity;
    private Double price;
    private String status;
}
