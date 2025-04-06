package org.example.electricstore.repository;

import org.example.electricstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User findByUsername(String username);

    boolean existsByEmailAndUserIdNot(String email, Integer userId);
}
