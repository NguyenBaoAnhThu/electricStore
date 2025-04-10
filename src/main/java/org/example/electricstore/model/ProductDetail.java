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

    private Double screenSize;
    private Integer camera;
    private Integer frontCamera;
    private String color;
    private String description;

    private String cpu;
    private String gpu;
    private String ram;
    private String rom;

    private String os;
    private String osVersion;
    private String battery;

    private String screenType;
    private String screenResolution;

    private String ports;
    private Integer weight;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}