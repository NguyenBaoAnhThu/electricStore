package org.example.electricstore.controller.admin;

import org.example.electricstore.DTO.product.ProductDTO;
import org.example.electricstore.mapper.product.ProductMapper;
import org.example.electricstore.model.Product;
import org.example.electricstore.service.common.CloudinaryService;
import org.example.electricstore.service.impl.BrandService;
import org.example.electricstore.service.impl.CategoryService;
import org.example.electricstore.service.impl.ProductService;
import org.example.electricstore.service.impl.SupplierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.beans.PropertyEditorSupport;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/Admin/product-manager")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping
    public String showListProduct(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "minPrice", required = false) Double minPrice,
            @RequestParam(name = "maxPrice", required = false) Double maxPrice,
            @RequestParam(name = "category", required = false) Integer categoryId,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "5") int size,
            @RequestParam(name = "message", required = false) String message,
            Model model) {

        Page<Product> productPage = productService.searchProducts(keyword, minPrice, maxPrice, categoryId, page - 1, size);
        List<Product> products = productPage.getContent();
        DecimalFormat decimalFormat = new DecimalFormat("#,### VND");

        for (Product product : products) {
            try {
                Double priceValue = (product.getPrice() != null) ? product.getPrice() : 0.0;
                product.setFormattedPrice(decimalFormat.format(priceValue));
            } catch (IllegalArgumentException e) {
                product.setFormattedPrice("0 VND");
            }
        }
        model.addAttribute("products", products);
        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("category", categoryId);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("suppliers",supplierService.getAllSuppliers());

        // ⚠ Xử lý nếu trang hiện tại vượt quá số trang thực tế
        if (page > productPage.getTotalPages() && productPage.getTotalPages() > 0) {
            int newPage = Math.max(1, productPage.getTotalPages()); // Quay về trang hợp lệ cuối cùng
            return "redirect:/Admin/product-manager?page=" + newPage +
                    (keyword != null ? "&keyword=" + URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "") +
                    (minPrice != null ? "&minPrice=" + minPrice : "") +
                    (maxPrice != null ? "&maxPrice=" + maxPrice : "") +
                    (categoryId != null ? "&category=" + categoryId : "");
        }

        // ⚠ Nếu tất cả dữ liệu bị xóa, quay về trang đầu tiên
        if (productPage.getTotalElements() == 0 && page > 1) {
            return "redirect:/Admin/product-manager?page=1" +
                    (keyword != null ? "&keyword=" + URLEncoder.encode(keyword, StandardCharsets.UTF_8) : "") +
                    (minPrice != null ? "&minPrice=" + minPrice : "") +
                    (maxPrice != null ? "&maxPrice=" + maxPrice : "") +
                    (categoryId != null ? "&category=" + categoryId : "");
        }

        // Thêm thông báo nếu có
        if (message != null) {
            model.addAttribute("message", message);
        }

        // Thêm thông báo khi không tìm thấy sản phẩm
        if (products.isEmpty() && (keyword != null || minPrice != null || maxPrice != null || categoryId != null)) {
            model.addAttribute("emptyMessage", "Không tìm thấy sản phẩm phù hợp với dữ liệu tìm kiếm!");
        }

        return "admin/product_brand_category/listProduct";
    }

    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Integer id, Model model) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            ProductDTO productDTO = productMapper.toDTO(product.get());
            model.addAttribute("product", productDTO);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("brands", brandService.getAllBrands());
            model.addAttribute("suppliers",supplierService.getAllSuppliers());
            return "admin/product_brand_category/editProduct";
        } else {
            return "redirect:/Admin/product-manager?message=Không tìm thấy sản phẩm!";
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Double.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text != null && !text.isEmpty()) {
                    // Xóa dấu phẩy trước khi convert
                    setValue(Double.parseDouble(text.replace(",", "")));
                }
            }
        });
    }

    @PostMapping("/edit")
    public String editProduct(@Valid @ModelAttribute("product") ProductDTO productDTO,
                              BindingResult bindingResult,
                              @RequestParam(value = "files", required = false)
                              List<MultipartFile> files,
                              Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("brands", brandService.getAllBrands());
            model.addAttribute("suppliers", supplierService.getAllSuppliers());
            model.addAttribute("mainImageUrl",productDTO.getMainImageUrl()) ;
            return "admin/product_brand_category/editProduct";
        }
        // Chuyển đổi từ DTO sang Entity
        Product product = productMapper.toEntity(productDTO);
        product.getProductDetail().setProduct(product);
        //  Lưu sản phẩm vào database
        productService.updateProduct(product, files);
        return "redirect:/Admin/product-manager";

    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("suppliers",supplierService.getAllSuppliers());
        return "admin/product_brand_category/addProduct";
    }

    // 🔹 Xử lý khi người dùng thêm sản phẩm
    @PostMapping("/add")
    public String addProduct(
            @Valid @ModelAttribute("product") ProductDTO productDTO,
            BindingResult bindingResult,
            @RequestParam("files") List<MultipartFile> files,
            Model model) {
        String imgLink = productDTO.getMainImageUrl();
        System.out.println(imgLink);
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("brands", brandService.getAllBrands());
            model.addAttribute("suppliers",supplierService.getAllSuppliers());
            model.addAttribute("mainImageUrl",productDTO.getMainImageUrl()) ;
            return "admin/product_brand_category/addProduct";
        }

        //  Kiểm tra ảnh có được tải lên không
        if (files == null || files.isEmpty()) {
            bindingResult.rejectValue("mainImageUrl", "error.product", "Vui lòng chọn ít nhất một ảnh!");
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("brands", brandService.getAllBrands());
            model.addAttribute("suppliers",supplierService.getAllSuppliers());
            return "admin/product_brand_category/addProduct";
        }

        // Chuyển đổi từ DTO sang Entity
        Product product = productMapper.toEntity(productDTO);

        product.getProductDetail().setProduct(product);

        //  Lưu sản phẩm vào database
        productService.saveProductWithDetailsAndImages(product, product.getProductDetail(), files);

        return "redirect:/Admin/product-manager"; // Chuyển hướng khi thêm thành công
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteProducts(@RequestBody List<Integer> productIds) {
        try {
            productService.deleteProduct(productIds);
            return ResponseEntity.ok().body("{\"success\": true, \"message\": \"Sản phẩm đã được xóa thành công!\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"Lỗi khi xóa sản phẩm!\"}");
        }
    }

}