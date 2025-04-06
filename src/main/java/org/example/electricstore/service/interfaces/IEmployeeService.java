package org.example.electricstore.service.interfaces;

import org.example.electricstore.DTO.employee.EmployeeDTO;
import org.example.electricstore.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface IEmployeeService {
    //Delete employee
    List<Employee> findByIds(List<Integer> employeeIds);
    List<String> getEmployeeNamesByIds(List<Integer> employeeIds);
    void saveAll(List<Employee> employees);
    Page<Employee> searchByFieldAndKeyword(String field, String keyword, int page, int size);
    Page<Employee> findAll(int page, int size);
    void save(EmployeeDTO employeeDTO);
    void update(EmployeeDTO employeeDTO, BindingResult bindingResult);
    EmployeeDTO findDTOById(int id);
    Boolean findById(int id);
    boolean existedByPhone(String phone);
}
