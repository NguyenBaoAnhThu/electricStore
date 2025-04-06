package org.example.electricstore.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "admins")
@Getter
@Setter
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adminId;
    private String adminName;
    private String department;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user ;
}
