package org.example.electricstore.service.impl;

import org.example.electricstore.model.Brand;
import org.example.electricstore.repository.IBrandRepository;
import org.example.electricstore.service.interfaces.IBrandService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BrandService implements IBrandService {

    private final IBrandRepository brandRepository;

    public BrandService(IBrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Optional<Brand> getBrandById(Integer brandID) {
        return brandRepository.findById(brandID);
    }

    @Override
    public void saveBrand(Brand brand) {
        if (brand.getBrandID() != null && brandRepository.existsById(brand.getBrandID())) {
            Brand existingBrand = brandRepository.findById(brand.getBrandID()).orElse(null);
            if (existingBrand != null) {
                existingBrand.setName(brand.getName());
                if (brand.getCountry() != null) {
                    existingBrand.setCountry(brand.getCountry());
                }
                existingBrand.setUpdateAt(LocalDateTime.now());
                brandRepository.save(existingBrand);
                brand.setCreateAt(existingBrand.getCreateAt());
            }
        } else {
            brand.setCreateAt(LocalDateTime.now());
            brand.setUpdateAt(LocalDateTime.now());
            brandRepository.save(brand);
        }
    }

    @Override
    public void deleteBrand(List<Integer> brandIds) {
        brandRepository.deleteAllById(brandIds);
    }

    @Override
    public List<Brand> findByNameContaining(String keyword) {
        return brandRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public boolean existsByName(String name) {
        return brandRepository.existsByName(name);
    }

    @Override
    public Page<Brand> getAllBrandsPaginated(int page, int size) {
        return brandRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Page<Brand> findByNameContainingPaginated(String name, int page, int size) {
        return brandRepository.findByNameContainingIgnoreCase(name, PageRequest.of(page, size));
    }
}