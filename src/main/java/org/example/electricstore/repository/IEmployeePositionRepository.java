package org.example.electricstore.repository;


import org.example.electricstore.model.EmployeePosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEmployeePositionRepository extends JpaRepository<EmployeePosition, Integer> {
}
