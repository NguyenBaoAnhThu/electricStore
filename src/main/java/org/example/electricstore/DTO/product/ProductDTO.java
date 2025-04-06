package org.example.electricstore.DTO.product;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private Integer productID;
    private String productDetail;

    //  Hỗ trợ tiếng Việt, số, khoảng trắng và dấu `-`
    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 100, message = "Tên sản phẩm không được dài quá 100 ký tự")
    @Pattern(regexp = "^[\\p{L}0-9\\s-]+$", message = "Tên sản phẩm không được chứa ký tự đặc biệt")
    private String name;

    private String mainImageUrl;

    //  Kiểm tra giá trị hợp lệ
    @NotNull(message = "Giá sản phẩm không được để trống")
    @DecimalMin(value = "1000.0", message = "Giá sản phẩm phải lớn hơn 1000 VND")
    @Digits(integer = 10, fraction = 2, message = "Giá sản phẩm không hợp lệ")
    private Double price;


    // Hỗ trợ tiếng Việt, số, khoảng trắng, dấu `.` và `,`
    @NotBlank(message = "Mô tả sản phẩm không được để trống")
    @Size(min = 10, message = "Mô tả phải có ít nhất 10 ký tự")
    @Pattern(regexp = "^[\\p{L}0-9.,\\s-]+$", message = "Mô tả sản phẩm không được chứa ký tự đặc biệt")
    private String description;

    @PastOrPresent(message = "Ngày tạo không hợp lệ")
    private LocalDateTime createAt;

    @PastOrPresent(message = "Ngày cập nhật không hợp lệ")
    private LocalDateTime updateAt;

    @NotNull(message = "Danh mục không được để trống")
    private Integer categoryId;

    @NotNull(message = "Thương hiệu không được để trống")
    private Integer brandId;

    @NotNull(message = "Nhà cung cấp không được để trống")
    private Integer id; // ID của Supplier

    // 🔹 Thông tin chi tiết sản phẩm
    @Min(value = 4, message = "Kích thước màn hình phải lớn hơn 4 inch")
    @Max(value = 20, message = "Kích thước màn hình không hợp lệ")
    private Integer screenSize;

    @Min(value = 0, message = "Camera phải có ít nhất 1 MP")
    private Integer camera;

    // ✅ Hỗ trợ màu sắc tiếng Việt, số và khoảng trắng
    @NotBlank(message = "Màu sắc không được để trống")
    @Pattern(regexp = "^[\\p{L}0-9\\s]+$", message = "Màu sắc không được chứa ký tự đặc biệt")
    private String color;

    // ✅ Hỗ trợ CPU có dấu `-`
    @NotBlank(message = "CPU không được để trống")
    @Pattern(regexp = "^[\\p{L}0-9\\s-]+$", message = "CPU không được chứa ký tự đặc biệt")
    private String cpu;

    // ✅ RAM & ROM: Hỗ trợ định dạng GB
    @NotBlank(message = "RAM không được để trống")
    @Pattern(regexp = "^[1-9][0-9]*GB$", message = "RAM phải có định dạng đúng, ví dụ: 8GB, 16GB")
    private String ram;

    @NotBlank(message = "ROM không được để trống")
    @Pattern(regexp = "^[1-9][0-9]*GB$", message = "ROM phải có định dạng đúng, ví dụ: 128GB, 256GB")
    private String rom;

    // ✅ Pin: Hỗ trợ định dạng mAh
    @NotBlank(message = "Dung lượng pin không được để trống")
    @Pattern(regexp = "^[1-9][0-9]*mAh$", message = "Pin phải có định dạng đúng, ví dụ: 4000mAh, 5000mAh")
    private String battery;


}
