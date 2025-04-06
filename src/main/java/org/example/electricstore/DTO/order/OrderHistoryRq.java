package org.example.electricstore.DTO.order;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderHistoryRq {

    private Integer id;
    private LocalDateTime orderDate;
    private String orderStatus;
    private String paymentMethod;
    private Double totalPrice;


}
