package org.example.electricstore.DTO.order;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOrderChoiceDTO {

    private Integer productId;
    private String productDetail;
    
    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 100, message = "Tên sản phẩm không được dài quá 100 ký tự")
    private String productName;
    
    @NotNull(message = "Giá sản phẩm không được để trống")
    @DecimalMin(value = "1000.0", message = "Giá sản phẩm phải lớn hơn 1000 VND")
    @Digits(integer = 10, fraction = 2, message = "Giá sản phẩm không hợp lệ")
    private String productPrice;
    
    @NotBlank(message = "CPU không được để trống")
    private String productCPU;

    @NotBlank(message = "RAM không được để trống")
    @Pattern(regexp = "^[1-9][0-9]*GB$", message = "RAM phải có định dạng đúng, ví dụ: 8GB, 16GB")
    private String productRam;
    
    @NotBlank(message = "ROM không được để trống")
    @Pattern(regexp = "^[1-9][0-9]*GB$", message = "ROM phải có định dạng đúng, ví dụ: 128GB, 256GB")
    private String productRom;
    
}
