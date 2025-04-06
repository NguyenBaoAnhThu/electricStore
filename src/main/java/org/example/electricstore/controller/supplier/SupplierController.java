package org.example.electricstore.controller.supplier;

import org.example.electricstore.DTO.supplier.SupplierDTO;
import org.example.electricstore.model.Product;
import org.example.electricstore.model.Supplier;
import org.example.electricstore.service.impl.SupplierService;
import org.example.electricstore.service.interfaces.IProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/Admin/suppliers-manager")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;
    @Autowired
    private IProductService productService;

    @GetMapping
    public String listSuppliers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String keyword,
            Model model) {

        // Thêm DTO rỗng cho modal thêm/sửa
        model.addAttribute("supplierDTO", new SupplierDTO());
        model.addAttribute("editSupplier", new SupplierDTO());

        Page<Supplier> supplierPage;

        if (filter != null && keyword != null && !keyword.trim().isEmpty()) {
            // Gọi phương thức tìm kiếm có phân trang
            supplierPage = searchByFilter(filter, keyword, page, size);
            model.addAttribute("isSearch", true);
        } else {
            // Lấy danh sách nhà cung cấp có phân trang
            supplierPage = supplierService.getSuppliers(page, size);
            model.addAttribute("isSearch", false);
        }

        // Xử lý trường hợp truy cập trang không hợp lệ
        if (page >= supplierPage.getTotalPages() && supplierPage.getTotalPages() > 0) {
            int newPage = Math.max(0, supplierPage.getTotalPages() - 1);
            return "redirect:/Admin/suppliers-manager?page=" + newPage + "&size=" + size;
        }
        if (supplierPage.getTotalElements() == 0 && page > 0) {
            return "redirect:/Admin/suppliers-manager?page=0&size=" + size;
        }

        // Thêm các thuộc tính phân trang vào model
        model.addAttribute("suppliers", supplierPage.getContent());
        model.addAttribute("currentPage", supplierPage.getNumber());
        model.addAttribute("totalPages", supplierPage.getTotalPages());
        model.addAttribute("totalItems", supplierPage.getTotalElements());
        model.addAttribute("pageSize", size);

        // Giữ lại giá trị tìm kiếm
        model.addAttribute("filter", filter);
        model.addAttribute("keyword", keyword);

        return "admin/suppliers/list";
    }

    // Phương thức tìm kiếm với phân trang
    private Page<Supplier> searchByFilter(String filter, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        switch (filter) {
            case "supplierCode":
                return supplierService.searchSuppliersByAllFields(keyword, null, null, null, null, pageable);
            case "name":
                return supplierService.searchSuppliersByAllFields(null, keyword, null, null, null, pageable);
            case "address":
                return supplierService.searchSuppliersByAllFields(null, null, keyword, null, null, pageable);
            case "phone":
                return supplierService.searchSuppliersByAllFields(null, null, null, keyword, null, pageable);
            case "email":
                return supplierService.searchSuppliersByAllFields(null, null, null, null, keyword, pageable);
            default:
                return supplierService.getSuppliers(page, size);
        }
    }

    @GetMapping("/{id}")
    public String getSupplier(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "0") int productPage,
            @RequestParam(defaultValue = "10") int productSize,
            Model model) {

        Optional<Supplier> supplier = supplierService.getSupplierById(id);
        if (supplier.isPresent()) {
            model.addAttribute("supplier", supplier.get());

            // Get paginated products for this supplier
            Page<Product> productsPage = productService.getProductsBySupplierPaginated(id, productPage, productSize);

            model.addAttribute("products", productsPage.getContent());
            model.addAttribute("productCurrentPage", productsPage.getNumber());
            model.addAttribute("productTotalPages", productsPage.getTotalPages());
            model.addAttribute("productTotalItems", productsPage.getTotalElements());
            model.addAttribute("productPageSize", productSize);

            return "admin/suppliers/details";
        }
        return "redirect:/Admin/suppliers-manager";
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public SupplierDTO getSupplierForEdit(@PathVariable Integer id) {
        Optional<Supplier> supplier = supplierService.getSupplierById(id);
        if (supplier.isPresent()) {
            Supplier s = supplier.get();
            SupplierDTO dto = new SupplierDTO();
            dto.setId(s.getId().longValue());
            dto.setSupplierCode(s.getSupplierCode());
            dto.setName(s.getName());
            dto.setAddress(s.getAddress());
            dto.setPhone(s.getPhone());
            dto.setEmail(s.getEmail());
            return dto;
        }
        return new SupplierDTO();
    }

    @PostMapping("/add")
    public String addSupplier(
            @Valid @ModelAttribute("supplierDTO") SupplierDTO supplierDTO,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.supplierDTO", bindingResult);
            redirectAttributes.addFlashAttribute("supplierDTO", supplierDTO);
            redirectAttributes.addFlashAttribute("showAddModal", true);
            return "redirect:/Admin/suppliers-manager";
        }
        try {
            Supplier supplier = new Supplier();
            supplier.setSupplierCode(supplierDTO.getSupplierCode());
            supplier.setName(supplierDTO.getName());
            supplier.setAddress(supplierDTO.getAddress());
            supplier.setPhone(supplierDTO.getPhone());
            supplier.setEmail(supplierDTO.getEmail());
            supplierService.addSupplier(supplier);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm nhà cung cấp thành công");
            return "redirect:/Admin/suppliers-manager";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi khi thêm nhà cung cấp: " + e.getMessage());
            redirectAttributes.addFlashAttribute("supplierDTO", supplierDTO);
            redirectAttributes.addFlashAttribute("showAddModal", true);
            return "admin/suppliers/list"; // Return to form with error
        }
    }

    @PostMapping("/edit")
    public String updateSupplier(
            @Valid @ModelAttribute("editSupplier") SupplierDTO supplierDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.editSupplier", bindingResult);
            redirectAttributes.addFlashAttribute("editSupplier", supplierDTO);
            redirectAttributes.addFlashAttribute("showEditModal", true);
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng kiểm tra lại thông tin nhà cung cấp");
            return "redirect:/Admin/suppliers-manager";
        }

        try {
            Supplier supplier = new Supplier();
            supplier.setId(supplierDTO.getId().intValue());
            supplier.setSupplierCode(supplierDTO.getSupplierCode());
            supplier.setName(supplierDTO.getName());
            supplier.setAddress(supplierDTO.getAddress());
            supplier.setPhone(supplierDTO.getPhone());
            supplier.setEmail(supplierDTO.getEmail());

            supplierService.updateSupplier(supplier.getId(), supplier);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật nhà cung cấp thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi cập nhật nhà cung cấp: " + e.getMessage());
            redirectAttributes.addFlashAttribute("editSupplier", supplierDTO);
            redirectAttributes.addFlashAttribute("showEditModal", true);
        }
        return "redirect:/Admin/suppliers-manager";
    }
    @PostMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteSuppliers(@RequestBody List<Integer> supplierIds) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Xóa danh sách nhà cung cấp
            supplierService.deleteSuppliers(supplierIds);

            // Gọi lại danh sách nhà cung cấp từ service để cập nhật totalPages
            int pageSize = 5; // Kích thước trang
            Page<Supplier> supplierPage = supplierService.getSuppliers(0, pageSize); // Lấy trang đầu tiên
            int totalPages = supplierPage.getTotalPages(); // Tổng số trang mới sau khi xóa

            // Đảm bảo totalPages không nhỏ hơn 1
            totalPages = Math.max(totalPages, 1);

            // Trả về phản hồi JSON
            response.put("success", true);
            response.put("message", "Danh mục đã được xóa thành công!");
            response.put("totalPages", totalPages);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi xóa danh mục!");
            return ResponseEntity.badRequest().body(response);
        }
    }


}