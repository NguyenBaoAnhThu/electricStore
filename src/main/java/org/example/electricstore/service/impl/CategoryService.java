package org.example.electricstore.service.impl;

import org.example.electricstore.model.Category;
import org.example.electricstore.repository.ICategoryRepository;
import org.example.electricstore.service.interfaces.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService implements ICategoryService {

    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Page<Category> getAllCategoriesPaged(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public List<Category> findByNameContaining(String keyword) {
        return categoryRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public Page<Category> findByNameContainingPaged(String keyword, Pageable pageable) {
        return categoryRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    @Override
    public List<Category> findByCategoryCodeContaining(String keyword) {
        return categoryRepository.findByCategoryCodeContainingIgnoreCase(keyword);
    }

    @Override
    public Page<Category> findByCategoryCodeContainingPaged(String keyword, Pageable pageable) {
        return categoryRepository.findByCategoryCodeContainingIgnoreCase(keyword, pageable);
    }

    @Override
    public Optional<Category> getCategoryById(Integer categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Override
    public void saveCategory(Category category) {
        if (category.getCategoryID() != null && categoryRepository.existsById(category.getCategoryID())) {
            Category existingCategory = categoryRepository.findById(category.getCategoryID()).orElse(null);
            if (existingCategory != null) {
                existingCategory.setName(category.getName());
                existingCategory.setCategoryCode(category.getCategoryCode());
                existingCategory.setDescription(category.getDescription());
                existingCategory.setUpdateAt(LocalDateTime.now());
                categoryRepository.save(existingCategory);
                category.setCreateAt(existingCategory.getCreateAt());
            }
        } else {
            category.setCreateAt(LocalDateTime.now());
            category.setUpdateAt(LocalDateTime.now());
            categoryRepository.save(category);
        }
    }

    @Override
    public void deleteCategory(List<Integer> categoryIds) {
        categoryRepository.deleteAllById(categoryIds);
    }

    @Override
    public boolean existsByName(String name) {
        return categoryRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean existsByNameAndNotId(String name, Integer id) {
        return categoryRepository.existsByNameIgnoreCaseAndCategoryIDNot(name, id);
    }

    @Override
    public boolean existsByCategoryCode(String categoryCode) {
        return categoryRepository.existsByCategoryCodeIgnoreCase(categoryCode);
    }

    @Override
    public boolean existsByCategoryCodeAndNotId(String categoryCode, Integer id) {
        return categoryRepository.existsByCategoryCodeIgnoreCaseAndCategoryIDNot(categoryCode, id);
    }

    @Override
    public boolean hasRelatedProducts(List<Integer> ids) {
        for (Integer id : ids) {
            Optional<Category> category = categoryRepository.findById(id);
            if (category.isPresent() && !category.get().getProducts().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}