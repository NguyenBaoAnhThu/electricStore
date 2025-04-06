package org.example.electricstore.service.impl;

import org.example.electricstore.DTO.order.ProductChosen;
import org.example.electricstore.DTO.order.ProductOrderChoiceDTO;
import org.example.electricstore.mapper.product.ProductMapper;
import org.example.electricstore.model.*;
import org.example.electricstore.repository.*;
import org.example.electricstore.service.common.CloudinaryService;
import org.example.electricstore.service.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService implements IProductService {
    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ProductDetailRepository productDetailRepository;
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ISupplierRepository supplierRepository;
    @Autowired
    private IBrandRepository brandRepository;
    @Autowired
    private ICategoryRepository categoryRepository;


    public ProductService(IProductRepository productRepository, ProductImageRepository productImageRepository, CloudinaryService cloudinaryService, ProductImageRepository productImageRepository1, ProductDetailRepository productDetailRepository) {

        this.productRepository = productRepository;

        this.productImageRepository = productImageRepository;
        this.productDetailRepository = productDetailRepository;
    }

    @Override
    @Transactional
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Integer productID) {

        return productRepository.findById(productID);
    }


    public Product findById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }



    @Override
    public void deleteProduct(List<Integer> productIds) {
        productRepository.deleteAllById(productIds);
    }

    public Page<Product> searchProducts(String keyword, Double minPrice, Double maxPrice, Integer categoryId, int page, int size) {
        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null;  // Bỏ qua nếu từ khóa rỗng
        }
        if (minPrice == null || minPrice < 0) {
            minPrice = null;  // Không giới hạn minPrice
        }
        if (maxPrice == null || maxPrice < 0) {
            maxPrice = null;  // Không giới hạn maxPrice
        }
        if (categoryId == null || categoryId == 0) {
            categoryId = null; // Không lọc theo category nếu không chọn
        }
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.searchProducts(categoryId, keyword, minPrice, maxPrice, pageable);
    }
    @Override
    public ProductChosen getProductByIdUseInOrder(Integer id) {
        Product product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return null;
        }

        ProductChosen chosen = new ProductChosen();
        chosen.setProductId(product.getProductID());
        chosen.setProductName(product.getName());
        chosen.setProductPrice(product.getPrice().intValue());
        chosen.setQuantity(1);
        return chosen;
    }
    @Override
    public Page<ProductOrderChoiceDTO> getProducts(String keyword, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);

        Page<Product> productPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            productPage = productRepository.findByNameContaining(keyword, pageRequest);
        } else {
            productPage = productRepository.findAll(pageRequest);
        }

        return productPage.map(productMapper::convertToProductChoiceDTOInOrder);
    }

    @Transactional
    @Override
    public void updateProduct(Product product, List<MultipartFile> files) {
        Optional<Product> existingProductOpt = productRepository.findById(product.getProductID());
        if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();

            if (product.getFormattedPrice() != null && !product.getFormattedPrice().isEmpty()) {
                try {
                    String cleanedPrice = product.getFormattedPrice().replaceAll("[^0-9]", "");
                    existingProduct.setPrice(Double.parseDouble(cleanedPrice));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            existingProduct.setName(product.getName());
            existingProduct.setCreateAt(product.getCreateAt());
            existingProduct.setUpdateAt(LocalDateTime.now());
            existingProduct.setMainImageUrl(product.getMainImageUrl());

            // Cập nhật nhà cung cấp
            if (product.getSupplier() != null && product.getSupplier().getId() != null) {
                Optional<Supplier> supplierOpt = supplierRepository.findById(product.getSupplier().getId());
                supplierOpt.ifPresent(existingProduct::setSupplier);
            }

            // Cập nhật danh mục
            if (product.getCategory() != null && product.getCategory().getCategoryID() != null) {
                Optional<Category> categoryOpt = categoryRepository.findById(product.getCategory().getCategoryID());
                categoryOpt.ifPresent(existingProduct::setCategory);
            }

            // Cập nhật thương hiệu
            if (product.getBrand() != null && product.getBrand().getBrandID() != null) {
                Optional<Brand> brandOpt = brandRepository.findById(product.getBrand().getBrandID());
                brandOpt.ifPresent(existingProduct::setBrand);
            }

            // Cập nhật thông tin chi tiết sản phẩm
            if (existingProduct.getProductDetail() != null && product.getProductDetail() != null) {
                ProductDetail existingDetail = existingProduct.getProductDetail();
                ProductDetail newDetail = product.getProductDetail();

                existingDetail.setScreenSize(newDetail.getScreenSize());
                existingDetail.setCamera(newDetail.getCamera());
                existingDetail.setColor(newDetail.getColor());
                existingDetail.setCpu(newDetail.getCpu());
                existingDetail.setRam(newDetail.getRam());
                existingDetail.setRom(newDetail.getRom());
                existingDetail.setBattery(newDetail.getBattery());
                existingDetail.setDescription(newDetail.getDescription());

                // Đảm bảo mối quan hệ hai chiều
                existingDetail.setProduct(existingProduct);
            }

            if (files != null && !files.isEmpty()) {
                List<ProductImage> newImages = new ArrayList<>();
                String mainImageUrl = existingProduct.getMainImageUrl();

                for (int i = 0; i < files.size(); i++) {
                    MultipartFile file = files.get(i);
                    if (!file.isEmpty()) {
                        String imageUrl = cloudinaryService.uploadFileToCloudinary(file);

                        // Nếu là ảnh đầu tiên, đặt làm ảnh chính
                        if (i == 0) {
                            mainImageUrl = imageUrl;
                        }

                        // Thêm ảnh mới vào danh sách
                        ProductImage productImage = new ProductImage();
                        productImage.setImageURL(imageUrl);
                        productImage.setProduct(existingProduct);
                        newImages.add(productImage);
                    }
                }

                // Xóa ảnh cũ trước khi thêm ảnh mới
//                productImageRepository.deleteByProduct(existingProduct);

                // Lưu ảnh mới
                if (!newImages.isEmpty()) {
                    productImageRepository.saveAll(newImages);
                }

                // Cập nhật ảnh chính
                existingProduct.setMainImageUrl(mainImageUrl);
            }

            // Lưu sản phẩm sau khi cập nhật
            productRepository.save(existingProduct);
        }
    }

    @Override
    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public ProductDetail saveProductDetail(ProductDetail productDetail) {
        return productDetailRepository.save(productDetail);
    }

    @Override
    @Transactional
    public List<ProductImage> saveProductImages(List<ProductImage> productImages) {
        return productImageRepository.saveAll(productImages);
    }

    @Override
    @Transactional
    public void saveProductWithImages(Product product, List<ProductImage> productImages) {
        // Lưu sản phẩm trước
        Product savedProduct = productRepository.save(product);

        // Gán sản phẩm đã lưu cho từng ảnh
        if (productImages != null && !productImages.isEmpty()) {
            for (ProductImage image : productImages) {
                image.setProduct(savedProduct);
            }

            // Lưu danh sách ảnh
            productImageRepository.saveAll(productImages);

            // Nếu có ảnh, đặt ảnh đầu tiên làm ảnh chính
            if (!productImages.isEmpty()) {
                savedProduct.setMainImageUrl(productImages.get(0).getImageURL());
                productRepository.save(savedProduct);
            }
        }
    }


    @Override
    @Transactional
    public Product saveProductWithDetailsAndImages(Product product, ProductDetail productDetail, List<MultipartFile> files) {
        try {
            // ✅ 1. Cập nhật thời gian trước khi lưu
            LocalDateTime now = LocalDateTime.now();
            product.setCreateAt(now);
            product.setUpdateAt(now);

            // ✅ 2. Gán ProductDetail vào Product trước khi lưu
            if (productDetail != null) {
                productDetail.setProduct(product);
                product.setProductDetail(productDetail);
                productDetail.setCreateAt(now);
                productDetail.setUpdateAt(now);
            }

            // ✅ 3. Lưu sản phẩm trước để có ID
            product = productRepository.save(product);

            // ✅ 4. Lưu ProductDetail sau khi Product có ID
            if (productDetail != null) {
                productDetailRepository.save(productDetail);
            }

            // ✅ 5. Xử lý lưu ảnh sản phẩm nếu có ảnh
            String mainImageUrl = null;
            List<ProductImage> productImages = new ArrayList<>();

            if (files != null && !files.isEmpty()) {
                for (int i = 0; i < files.size(); i++) {
                    MultipartFile file = files.get(i);
                    if (!file.isEmpty()) {
                        String imageUrl = cloudinaryService.uploadFileToCloudinary(file);
                        if (i == 0) {
                            mainImageUrl = imageUrl; // ✅ Ảnh đầu tiên là ảnh chính
                        }
                        ProductImage productImage = new ProductImage();
                        productImage.setImageURL(imageUrl);
                        productImage.setProduct(product);
                        productImages.add(productImage);
                    }
                }
            }

            // ✅ 6. Lưu danh sách ảnh nếu có
            if (!productImages.isEmpty()) {
                productImageRepository.saveAll(productImages);
            }

            // ✅ 7. Cập nhật ảnh chính cho sản phẩm
            product.setMainImageUrl(mainImageUrl);
            productRepository.save(product);

            return product;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi lưu sản phẩm: " + e.getMessage());
        }
    }
    @Override
    public List<Product> getProductsBySupplier(Integer supplierId) {
        return productRepository.findBySupplierId(supplierId);
    }

    @Override
    public Boolean saveImagesToProduct(String ImageURL) {
        return null;
    }

    @Override
    public Page<Product> getProductsBySupplierPaginated(Integer supplierId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findBySupplierId(supplierId, pageable);
    }
}