package org.example.electricstore.DTO.statistical;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DailyRevenueDTO {
    private Integer day ;
    private int month;
    private int year;
    private Double revenue;
}
