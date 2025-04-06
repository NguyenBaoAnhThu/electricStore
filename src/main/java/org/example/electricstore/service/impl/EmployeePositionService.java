package org.example.electricstore.service.impl;

import org.example.electricstore.model.EmployeePosition;
import org.example.electricstore.repository.IEmployeePositionRepository;
import org.example.electricstore.service.interfaces.IEmployeePositionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeePositionService implements IEmployeePositionService<EmployeePosition> {
    private final IEmployeePositionRepository employeePositionRepository;

    public EmployeePositionService(IEmployeePositionRepository employeePositionRepository) {
        this.employeePositionRepository = employeePositionRepository;
    }

    @Override
    public List<EmployeePosition> getEmployeePositions() {
        return this.employeePositionRepository.findAll();
    }
    public EmployeePosition save(EmployeePosition employeePosition) {
        return this.employeePositionRepository.save(employeePosition);
    }
    @Override
    public void deleteById(Integer id) {
        this.employeePositionRepository.deleteById(id);
    }
}
