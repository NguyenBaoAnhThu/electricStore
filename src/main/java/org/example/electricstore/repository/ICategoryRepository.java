package org.example.electricstore.repository;

import org.example.electricstore.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByNameContainingIgnoreCase(String keyword);
    Page<Category> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndCategoryIDNot(String name, Integer id);
}
