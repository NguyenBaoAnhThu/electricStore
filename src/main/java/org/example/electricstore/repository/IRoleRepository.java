package org.example.electricstore.repository;


import org.example.electricstore.enums.RoleEnums;
import org.example.electricstore.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface IRoleRepository extends JpaRepository<Role, Integer> {
   Role findByRoleName (RoleEnums roleEnums) ;
   Optional<Role> findByRoleName(String roleName);
}
