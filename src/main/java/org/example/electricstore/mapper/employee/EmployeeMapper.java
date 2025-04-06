package org.example.electricstore.mapper.employee;

import org.example.electricstore.DTO.employee.EmployeeDTO;
import org.example.electricstore.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public Employee convertToEmployee(EmployeeDTO employeeDTO) {

        return Employee.builder()
                .employeeName(employeeDTO.getEmployeeName())
                .employeeAddress(employeeDTO.getEmployeeAddress())
                .employeePhone(employeeDTO.getEmployeePhone())
                .employeeBirthday(employeeDTO.getEmployeeBirthday())
                .isDisabled(false)
                .build();
    }
    public Employee convertToEmployeeHasId(EmployeeDTO employeeDTO) {

        return Employee.builder()
                .employeeId(employeeDTO.getEmployeeId())
                .employeeName(employeeDTO.getEmployeeName())
                .employeeAddress(employeeDTO.getEmployeeAddress())
                .employeePhone(employeeDTO.getEmployeePhone())
                .employeeBirthday(employeeDTO.getEmployeeBirthday())


                .build();
    }

    public EmployeeDTO convertToEmployeeDTO(Employee employee) {
        return EmployeeDTO.builder()
                .employeeId(employee.getEmployeeId())
                .employeeName(employee.getEmployeeName())
                .employeeAddress(employee.getEmployeeAddress())
                .employeePhone(employee.getEmployeePhone())
                .employeeBirthday(employee.getEmployeeBirthday())
                .email(employee.getUser().getEmail())
                .employeePosition(employee.getEmployeePosition().getPositionId())

                .username(employee.getUser().getUsername())
                .password(employee.getUser().getEncrytedPassword())
                .isDisabled(employee.getIsDisabled())
                .userId(employee.getUser().getUserId())
                .isResetPassword(false)


//                .employeeWork(employee.getEmployeeWork())
                .build();
    }
}
