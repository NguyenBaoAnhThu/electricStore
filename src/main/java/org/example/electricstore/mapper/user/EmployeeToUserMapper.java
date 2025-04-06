package org.example.electricstore.mapper.user;

import org.example.electricstore.DTO.employee.EmployeeDTO;
import org.example.electricstore.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EmployeeToUserMapper {
    private final PasswordEncoder passwordEncoder; // Inject PasswordEncoder

    public EmployeeToUserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User convertEmployeeToUser(EmployeeDTO employeeDTO) {

        String hashedPassword = passwordEncoder.encode(employeeDTO.getPassword());

        return User.builder()
                .email(employeeDTO.getEmail())
                .username(employeeDTO.getUsername())
                .encrytedPassword(hashedPassword) // Set hashed password
                .enabled(true)
                .build();
    }
}
