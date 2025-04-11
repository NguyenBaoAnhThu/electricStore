package org.example.electricstore.repository;

import org.example.electricstore.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBrandRepository extends JpaRepository<Brand, Integer> {
    List<Brand> findByNameContainingIgnoreCase(String name);
    boolean existsByName(String name);
    Page<Brand> findByNameContainingIgnoreCase(String name, Pageable pageable);
}