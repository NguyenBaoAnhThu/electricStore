package org.example.electricstore.DTO.statistical;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
    public class RevenueDetailDTO {

        private Integer id;
        private String productName;
        private Integer quantitySold;
        private Double sellingPrice;
        private Double importPrice;
        private Double totalSellingPrice;
        private Double totalImportCost;
        private Double profit;
        private Double profitRate ;

    }
