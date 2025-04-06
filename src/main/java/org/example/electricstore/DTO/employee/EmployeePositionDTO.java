package org.example.electricstore.DTO.employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeePositionDTO {
    private Integer positionId;

    @NotBlank(message = "Tên chức vụ không được để trống")
    @Size(min = 2, max = 100, message = "Tên chức vụ phải từ 2 đến 100 ký tự")
    private String positionName;

    @NotBlank(message = "Mô tả chức vụ không được để trống")
    @Size(min = 10, max = 500, message = "Mô tả chức vụ phải từ 10 đến 500 ký tự")
    private String positionDescription;
}
