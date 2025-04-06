package org.example.electricstore.DTO.warehouse;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseDTO {
    @NotNull(message = "Sản phẩm không được để trống")
    @Positive(message = "ID sản phẩm phải là số dương")
    private Integer productId;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    @Max(value = 1000000, message = "Số lượng không được vượt quá 1,000,000")
    private Integer quantity;

    @NotNull(message = "Giá nhập không được để trống")
    @DecimalMin(value = "0.01", message = "Giá nhập phải lớn hơn 0")
    @DecimalMax(value = "1000000000.00", message = "Giá nhập không được vượt quá 1 tỷ")
    private Double price;

}