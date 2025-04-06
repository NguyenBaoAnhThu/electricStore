package org.example.electricstore.repository;

import org.example.electricstore.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, Integer> {
        @Query("SELECT u FROM Employee u WHERE " +
            "(:type = 'all' AND (LOWER(u.employeeName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR u.employeePhone LIKE CONCAT('%', :keyword, '%') OR LOWER(u.employeeWork) LIKE LOWER(CONCAT('%', :keyword, '%')))) " +
            "OR (:type = 'employeeName' AND LOWER(u.employeeName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "OR (:type = 'employeePhone' AND u.employeePhone LIKE CONCAT('%', :keyword, '%')) " +
            "OR (:type = 'employeeWork' AND LOWER(u.employeeWork) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Employee> searchByKeywordAndType(String keyword, String type, Pageable pageable);
    boolean existsByEmployeePhone(String phone);
    Page<Employee> findByEmployeePosition_PositionNameContaining(String positionName, Pageable pageable);

    @Query("SELECT e from Employee e where "  +
            "(:field = 'name' AND e.employeeName LIKE %:keyword%) OR " +
            "(:field = 'phone' AND e.employeePhone LIKE %:keyword%)")
    Page<Employee> searchEmployees (@Param("field") String field,
                                    @Param("keyword") String keyword,
                                    Pageable pageable);
    Employee findEmployeeByUser_Username (String username);
    boolean existsByEmployeePhoneAndEmployeeIdNot(String phone, Integer id);
}