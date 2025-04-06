package org.example.electricstore.DTO.supplier;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierDTO {

    // ID should be positive if present
    @jakarta.validation.constraints.Min(value = 1, message = "ID phải là số dương")
    private Long id;

    @NotBlank(message = "Mã nhà cung cấp không được để trống")
    @Size(min = 3, max = 20, message = "Mã nhà cung cấp phải có độ dài từ 3 đến 20 ký tự")
    @Pattern(regexp = "^[A-Z0-9-]+$",
            message = "Mã nhà cung cấp chỉ được chứa chữ cái in hoa, số và dấu gạch ngang")
    private String supplierCode;

    @NotBlank(message = "Tên không được để trống")
    @Size(min = 2, max = 50, message = "Tên phải có độ dài từ 2 đến 50 ký tự")
    @Pattern(regexp = "^[A-Za-zÀ-ỹ\\s]+$",
            message = "Tên chỉ được chứa chữ cái và khoảng trắng")
    private String name;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(min = 1, max = 200, message = "Địa chỉ phải có độ dài từ 5 đến 200 ký tự")
    @Pattern(regexp = "^[A-Za-z0-9À-ỹ,\\s.-]+$",
            message = "Địa chỉ chỉ được chứa chữ cái, số, khoảng trắng, dấu chấm, dấu phẩy và dấu gạch ngang")
    private String address;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(\\+84|0)[35789][0-9]{8}$",
            message = "Số điện thoại phải bắt đầu bằng +84 hoặc 0, theo sau là 3,5,7,8,9 và 8 chữ số")
    private String phone;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ, vui lòng nhập đúng định dạng")
    @Size(min = 5, max = 100, message = "Email phải có độ dài từ 5 đến 100 ký tự")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email phải theo định dạng chuẩn (ví dụ: example@domain.com)")
    private String email;
}