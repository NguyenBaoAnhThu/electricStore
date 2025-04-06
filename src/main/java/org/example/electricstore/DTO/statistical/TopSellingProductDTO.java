package org.example.electricstore.DTO.statistical;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopSellingProductDTO {

    private Integer id;
    private String name;
    private Long quantitySold;
    private Double totalRevenue;
}
