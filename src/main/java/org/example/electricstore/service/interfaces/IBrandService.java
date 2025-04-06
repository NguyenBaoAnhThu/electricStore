package org.example.electricstore.service.interfaces;

import org.example.electricstore.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IBrandService {
    List<Brand> getAllBrands();
    Optional<Brand> getBrandById(Integer brandID);
    void saveBrand(Brand brand);
    void deleteBrand(List<Integer> brandIds);
    boolean existsByName(String name);
    Page<Brand> getAllBrandsPaginated(Pageable pageable);
    Page<Brand> findByNameContainingPaginated(String name, Pageable pageable);
    List<Brand> findByNameContaining(String keyword);
}