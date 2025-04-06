package org.example.electricstore.service.interfaces;

import org.example.electricstore.DTO.order.ProductChosen;
import org.example.electricstore.DTO.order.ProductOrderChoiceDTO;
import org.example.electricstore.model.Product;
import org.example.electricstore.model.ProductDetail;
import org.example.electricstore.model.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    List<Product> getAllProducts();
    Optional<Product> getProductById(Integer productID);
    void deleteProduct(List<Integer> productIds);
    Product findById(Integer id);
     void updateProduct(Product product, List<MultipartFile> files);
    //Choose product in order
    Page<ProductOrderChoiceDTO> getProducts(String keyword, Integer page, Integer size);
    ProductChosen getProductByIdUseInOrder(Integer id);

    Product saveProduct(Product product);
    ProductDetail saveProductDetail(ProductDetail productDetail);
    void saveProductWithImages(Product product, List<ProductImage> productImages);
    List<ProductImage> saveProductImages(List<ProductImage> productImages);
    Product saveProductWithDetailsAndImages(Product product, ProductDetail productDetail, List<MultipartFile> files);
    List<Product> getProductsBySupplier(Integer supplierId);
    Boolean saveImagesToProduct(String ImageURL);
    Page<Product> getProductsBySupplierPaginated(Integer supplierId, int page, int size);
}
