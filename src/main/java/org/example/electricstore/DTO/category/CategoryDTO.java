package org.example.electricstore.DTO.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Integer categoryID;

    @NotBlank(message = "Tên danh mục không được để trống")
    @Size(min = 2, max = 100, message = "Tên danh mục phải từ 2 đến 100 ký tự")
    @Pattern(regexp = "^[\\p{L}0-9\\s]+$", message = "Tên danh mục không được chứa ký tự đặc biệt")
    private String name;

    @NotBlank(message = "Mô tả không được để trống")
    @Size(min = 10, max = 1000, message = "Mô tả phải từ 10 đến 1000 ký tự")
    @Pattern(regexp = "^[\\p{L}0-9\\s,.]+$", message = "Mô tả không được chứa ký tự đặc biệt ngoại trừ dấu chấm và dấu phẩy")
    private String description;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;
}
