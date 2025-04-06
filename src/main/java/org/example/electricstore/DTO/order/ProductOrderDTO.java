package org.example.electricstore.DTO.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOrderDTO {

    @NotNull(message = "Mã sản phẩm không được để trống !")
    private Integer productId;

    @NotNull(message = "Tên sản phẩm không được để trống !")
    private String productName;

    @NotNull(message = "Số lượng không được để trống !")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0 !")
    private Integer quantity;
    @NotNull(message = "Giá không được để trống !")

    @Min(value = 1, message = "Giá phải lớn hơn 0 !")
    private Integer priceIndex;

    @Override
    public String toString() {
        return "ProductOrderDTO{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", priceIndex=" + priceIndex +
                '}';
    }
}
