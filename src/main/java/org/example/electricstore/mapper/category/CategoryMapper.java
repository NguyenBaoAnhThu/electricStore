package org.example.electricstore.mapper.category;

import org.example.electricstore.DTO.category.CategoryDTO;
import org.example.electricstore.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDTO toDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryID(category.getCategoryID());
        dto.setCategoryCode(category.getCategoryCode());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setCreateAt(category.getCreateAt());
        dto.setUpdateAt(category.getUpdateAt());
        return dto;
    }

    public Category toEntity(CategoryDTO dto) {
        Category category = new Category();
        category.setCategoryID(dto.getCategoryID());
        category.setCategoryCode(dto.getCategoryCode());
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setCreateAt(dto.getCreateAt());
        category.setUpdateAt(dto.getUpdateAt());
        return category;
    }

    public void updateEntityFromDto(CategoryDTO dto, Category category) {
        category.setCategoryCode(dto.getCategoryCode());
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
    }
}