package org.example.electricstore.service.impl;

import org.example.electricstore.DTO.employee.EmployeeDTO;
import org.example.electricstore.enums.RoleEnums;
import org.example.electricstore.mapper.employee.EmployeeMapper;
import org.example.electricstore.mapper.user.EmployeeToUserMapper;
import org.example.electricstore.model.Employee;
import org.example.electricstore.model.EmployeePosition;
import org.example.electricstore.model.Role;
import org.example.electricstore.model.User;
import org.example.electricstore.repository.IEmployeePositionRepository;
import org.example.electricstore.repository.IEmployeeRepository;
import org.example.electricstore.repository.IRoleRepository;
import org.example.electricstore.repository.IUserRepository;
import org.example.electricstore.service.interfaces.IEmployeeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService implements IEmployeeService {

    @Autowired
    PasswordEncoder passwordEncoder;

    private final IEmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final IUserRepository iUserRepository;
    private final EmployeeToUserMapper employeeToUserMapper;
    private final IEmployeePositionRepository employeePositionRepository;
    private final IRoleRepository roleRepository;
    public EmployeeService(IEmployeeRepository employeeRepository ,
                           EmployeeMapper employeeMapper ,
                           IUserRepository iUserRepository ,
                           EmployeeToUserMapper employeeToUserMapper ,
                           IEmployeePositionRepository employeePositionRepository ,
                           IRoleRepository roleRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.iUserRepository = iUserRepository;
        this.employeeToUserMapper = employeeToUserMapper;
        this.employeePositionRepository = employeePositionRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Employee> findByIds(List<Integer> employeeIds) {
        return employeeRepository.findAllById(employeeIds);
    }

    @Override
    public List<String> getEmployeeNamesByIds(List<Integer> employeeIds) {
        return employeeRepository.findAllById(employeeIds).stream().map(Employee::getEmployeeName).toList();
    }

    @Override
    public void saveAll(List<Employee> employees) {
        employeeRepository.saveAll(employees);
    }


    @Transactional
    public boolean disableEmployees(List<Integer> employeeIds) {
        try {
            List<Employee> employees = employeeRepository.findAllById(employeeIds);
            for (Employee emp : employees) {
                emp.setIsDisabled(true);
            }
            employeeRepository.saveAll(employees);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public boolean activateEmployees(List<Integer> employeeIds) {
        try {
            List<Employee> employees = employeeRepository.findAllById(employeeIds);
            for (Employee emp : employees) {
                emp.setIsDisabled(false);
            }
            employeeRepository.saveAll(employees);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Page<Employee> searchByFieldAndKeyword(String field, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        if ("work".equals(field)) {
            return this.employeeRepository.findByEmployeePosition_PositionNameContaining(keyword, pageable);
        }
        return this.employeeRepository.searchEmployees(field, keyword, pageable);
    }

    @Override
    public Page<Employee> findAll(int page, int size) {
        return this.employeeRepository.findAll(PageRequest.of(page - 1, size));
    }



    @Override
    public void save(EmployeeDTO employeeDTO) {
        if (this.employeeRepository.existsByEmployeePhone(employeeDTO.getEmployeePhone())) {
            throw new EntityNotFoundException("Employee Phone already exists");
        }
        if (this.iUserRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new EntityNotFoundException("Email already exists");
        }
        if (this.iUserRepository.existsByUsername(employeeDTO.getUsername())) {
            throw new EntityNotFoundException("Username already exists");
        }

        User user = this.employeeToUserMapper.convertEmployeeToUser(employeeDTO);

        EmployeePosition employeePosition =
                this.employeePositionRepository.findById(employeeDTO.getEmployeePosition())
                        .orElseThrow(() -> new EntityNotFoundException("Employee Position not found"));
        List<Role> roleList = new ArrayList<>() ;
        Role role ;
        switch (employeePosition.getPositionName()) {
            case "Nhân Viên Kinh Doanh":
                 role = this.roleRepository.findByRoleName(RoleEnums.ROLE_BUSINESS) ;
                roleList.add(role);
                break;
            case "Nhân Viên Bán Hàng":
                 role = this.roleRepository.findByRoleName(RoleEnums.ROLE_SALES) ;
                roleList.add(role);
                break;
            case "Nhân Viên Thủ Kho":
                role = this.roleRepository.findByRoleName(RoleEnums.ROLE_WAREHOUSE) ;
                roleList.add(role);
                break;
        }
        user.setRoles(roleList);
        this.iUserRepository.save(user);

        Employee employee = this.employeeMapper.convertToEmployee(employeeDTO);
        employee.setUser(user);
        employee.setEmployeePosition(employeePosition);

        this.employeeRepository.save(employee);
    }


    /**
     * @author SinhPH
     */
    @Override
    public void update(EmployeeDTO employeeDTO, BindingResult bindingResult) {

        User user = this.iUserRepository.findById(employeeDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // check user email
        if (!user.getEmail().equals(employeeDTO.getEmail())) {
            boolean emailExists = iUserRepository.existsByEmail(employeeDTO.getEmail());
            if (emailExists) {
                bindingResult.rejectValue("email", "error.employeeDTO", "Email đã tồn tại!");
                return;
            }
            user.setEmail(employeeDTO.getEmail());
        }

        // check user password
        if(employeeDTO.getPassword() != null && !employeeDTO.getPassword().isEmpty()){
            user.setEncrytedPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        }

        //check phone
        Employee employeeCheck = this.employeeRepository.findById(employeeDTO.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        if (!employeeCheck.getEmployeePhone().equals(employeeDTO.getEmployeePhone())) {
            boolean phoneExists = this.employeeRepository.existsByEmployeePhone(employeeDTO.getEmployeePhone());
            if (phoneExists) {
                bindingResult.rejectValue("employeePhone", "error.employeeDTO", "Số điện thoại đã tồn tại!");
                return;
            }
        }

        //check birthDay must more than 15 year old if has change
        if(!employeeCheck.getEmployeeBirthday().equals(employeeDTO.getEmployeeBirthday()) &&
                employeeDTO.getEmployeeBirthday().getYear() >= 2010){
            bindingResult.rejectValue("employeeBirthday", "error.employeeDTO", "Ngày sinh phải lớn hơn 15 tuổi!");
            return;
        }


        // update employee
        Employee employee = this.employeeMapper.convertToEmployeeHasId(employeeDTO);
        EmployeePosition employeePosition =
                this.employeePositionRepository.findById(employeeDTO.getEmployeePosition()).orElseThrow(null);
        employee.setEmployeePosition(employeePosition);
        employee.setUser(user);

        this.employeeRepository.save(employee);

    }


    @Override
    public EmployeeDTO findDTOById(int id) {
        Employee employee = this.employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        return this.employeeMapper.convertToEmployeeDTO(employee);
    }

    @Override
    public Boolean findById(int id) {
        return this.employeeRepository.existsById(id);
    }

    @Override
    public boolean existedByPhone(String phone) {
        return this.employeeRepository.existsByEmployeePhone(phone);
    }
}

