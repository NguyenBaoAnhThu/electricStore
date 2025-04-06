package org.example.electricstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "brands")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer brandID;
    private String name;
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean status = true; // Trạng thái (1: Active, 0: Inactive)
    private String country; // Quốc gia xuất xứ
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createAt = now;
        updateAt = now;
    }
    @PreUpdate
    public void preUpdate() {
        updateAt = LocalDateTime.now();
    }
}
