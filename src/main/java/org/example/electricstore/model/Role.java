package org.example.electricstore.model;

import org.example.electricstore.enums.RoleEnums;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "role",
        uniqueConstraints = {
                @UniqueConstraint(name = "APP_ROLE_UK", columnNames = "role_name") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false, updatable = false)
    private Integer roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false, length = 30)
    private RoleEnums roleName;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}
