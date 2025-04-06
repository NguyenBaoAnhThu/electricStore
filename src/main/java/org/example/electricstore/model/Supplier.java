package org.example.electricstore.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "supplier_code", unique = true, nullable = false)
    private String supplierCode;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "address")
    private String address;
    @Column(name = "phone", unique = true)
    private String phone;
    @Column(name = "email", unique = true)
    private String email;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

}