package org.example.electricstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer screenSize;
    private Integer camera;
    private String color;

    @Column(name = "cpu")
    private String cpu;

    @Column(name = "ram")
    private String ram;

    @Column(name = "rom")
    private String rom;

    @Column(name = "battery")
    private String battery;

    private String description;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false) // Đảm bảo không null
    private Product product;
}


