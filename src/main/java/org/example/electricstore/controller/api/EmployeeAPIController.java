package org.example.electricstore.controller.api;

import org.example.electricstore.DTO.employee.EmployeeDTO;
import org.example.electricstore.service.impl.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
public class EmployeeAPIController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("getUserById/{id}")
    public EmployeeDTO getUserById(@PathVariable Integer id) {
        return employeeService.findDTOById(id);
    }

    @PostMapping("/edit")
    public Map<String, Object> edit(@RequestBody @Validated  EmployeeDTO employeeDTO, BindingResult bindingResult) {
        boolean hasOtherErrors = bindingResult.getFieldErrors().stream()
                .anyMatch(error -> !error.getField().equals("password"));
        Map<String, Object> responsive = new HashMap<>();

        if (hasOtherErrors) {
            Map<String,String> errors = new HashMap<>();
            for(FieldError fieldError : bindingResult.getFieldErrors()){
                errors.put(fieldError.getField(),fieldError.getDefaultMessage());
            }
            responsive.put("errors", errors);
            responsive.put("status", "error");
            return responsive;
        }
        System.out.println("employeeDTO = " + employeeDTO);
        employeeService.update(employeeDTO,bindingResult);

        // Kiểm tra nếu có lỗi từ service
        hasOtherErrors = bindingResult.getFieldErrors().stream()
                .anyMatch(error -> !error.getField().equals("password"));
        if (hasOtherErrors) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            responsive.put("errors", errors);
            responsive.put("status", "error");
            return responsive;
        }

        responsive.put("status", "success");
        responsive.put("message", "Cập nhật thành công");
        return responsive;

    }
}
