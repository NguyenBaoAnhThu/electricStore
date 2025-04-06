package org.example.electricstore.DTO.statistical;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevenueSummaryDTO {

    private Integer totalOrdersSuccess;
    private Integer totalItems;
    private Double revenue;
    private Integer totalOrdersFailed ;
}
