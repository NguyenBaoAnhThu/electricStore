package org.example.electricstore.repository;

import org.example.electricstore.model.User;
import org.example.electricstore.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IUserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findAllByUser(User user);
}