package org.example.electricstore.service.interfaces;

import org.example.electricstore.model.Brand;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IBrandService {
    List<Brand> getAllBrands();
    Optional<Brand> getBrandById(Integer brandID);
    void saveBrand(Brand brand);
    void deleteBrand(List<Integer> brandIds);
    boolean existsByName(String name);
    Page<Brand> getAllBrandsPaginated(int page, int size);
    Page<Brand> findByNameContainingPaginated(String name, int page, int size);
    List<Brand> findByNameContaining(String keyword);
}