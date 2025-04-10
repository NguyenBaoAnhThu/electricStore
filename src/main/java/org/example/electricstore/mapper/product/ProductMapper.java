package org.example.electricstore.mapper.product;

import org.example.electricstore.DTO.order.ProductOrderChoiceDTO;
import org.example.electricstore.DTO.product.ProductChoiceDTO;
import org.example.electricstore.DTO.product.ProductDTO;
import org.example.electricstore.model.*;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductDTO dto) {
        if (dto == null) return null;

        Product product = new Product();
        product.setProductID(dto.getProductID());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setCreateAt(dto.getCreateAt());
        product.setUpdateAt(dto.getUpdateAt());
        product.setMainImageUrl(dto.getMainImageUrl());
        product.setProductCode(dto.getProductCode());

        Category category = new Category();
        category.setCategoryID(dto.getCategoryId());
        product.setCategory(category);

        Brand brand = new Brand();
        brand.setBrandID(dto.getBrandId());
        product.setBrand(brand);

        Supplier supplier = new Supplier();
        supplier.setId(dto.getId());
        product.setSupplier(supplier);

        // Create product detail
        ProductDetail detail = new ProductDetail();
        detail.setScreenSize(dto.getScreenSize());
        detail.setCamera(dto.getCamera());
        detail.setFrontCamera(dto.getFrontCamera());
        detail.setColor(dto.getColor());
        detail.setCpu(dto.getCpu());
        detail.setGpu(dto.getGpu());
        detail.setRam(dto.getRam());
        detail.setRom(dto.getRom());
        detail.setOs(dto.getOs());
        detail.setOsVersion(dto.getOsVersion());
        detail.setBattery(dto.getBattery());
        detail.setScreenType(dto.getScreenType());
        detail.setScreenResolution(dto.getScreenResolution());
        detail.setPorts(dto.getPorts());
        detail.setWeight(dto.getWeight());
        detail.setProduct(product);

        product.setProductDetail(detail);
        return product;
    }

    public ProductDTO toDTO(Product product) {
        if (product == null) return null;

        ProductDTO dto = new ProductDTO();
        dto.setProductID(product.getProductID());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        dto.setCreateAt(product.getCreateAt());
        dto.setUpdateAt(product.getUpdateAt());
        dto.setMainImageUrl(product.getMainImageUrl());
        dto.setId(product.getSupplier().getId());
        dto.setProductCode(product.getProductCode());

        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getCategoryID());
        }
        if (product.getBrand() != null) {
            dto.setBrandId(product.getBrand().getBrandID());
        }
        if (product.getSupplier() != null) {
            dto.setSupplierId(product.getSupplier().getId());
        }

        if (product.getProductDetail() != null) {
            ProductDetail detail = product.getProductDetail();
            dto.setScreenSize(detail.getScreenSize());
            dto.setCamera(detail.getCamera());
            dto.setFrontCamera(detail.getFrontCamera());
            dto.setColor(detail.getColor());
            dto.setCpu(detail.getCpu());
            dto.setGpu(detail.getGpu());
            dto.setRam(detail.getRam());
            dto.setRom(detail.getRom());
            dto.setOs(detail.getOs());
            dto.setOsVersion(detail.getOsVersion());
            dto.setBattery(detail.getBattery());
            dto.setScreenType(detail.getScreenType());
            dto.setScreenResolution(detail.getScreenResolution());
            dto.setPorts(detail.getPorts());
            dto.setWeight(detail.getWeight());
        }

        return dto;
    }

    public ProductChoiceDTO convertToProductChoiceDTO(Product product) {
        return ProductChoiceDTO.builder()
                .productId(product.getProductID())
                .productName(product.getName())
                .supplierName(product.getSupplier().getName())
                .productPrice(product.getPrice())
                .build();
    }

    public ProductChoiceDTO convertToProductChoiceDTOByWareHouse(WareHouse wareHouse) {
        return ProductChoiceDTO.builder()
                .productId(wareHouse.getProduct().getProductID())
                .productName(wareHouse.getProduct().getName())
                .supplierName(wareHouse.getProduct().getSupplier().getName())
                .productQuantity(wareHouse.getQuantity())
                .productPrice(wareHouse.getPrice())
                .build();
    }

    public ProductOrderChoiceDTO convertToProductChoiceDTOInOrder(Product product) {
        return ProductOrderChoiceDTO.builder()
                .productId(product.getProductID())
                .productName(product.getName())
                .productPrice(product.getFormattedPrice())
                .productCPU(product.getProductDetail() != null ? product.getProductDetail().getCpu() : "Không có dữ liệu")
                .productRam(product.getProductDetail() != null ? product.getProductDetail().getRam() : "Không có dữ liệu")
                .productRom(product.getProductDetail() != null ? product.getProductDetail().getRom() : "Không có dữ liệu")
                .build();
    }
}