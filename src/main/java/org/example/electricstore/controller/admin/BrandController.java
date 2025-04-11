package org.example.electricstore.controller.admin;

import org.example.electricstore.DTO.brand.BrandDTO;
import org.example.electricstore.model.Brand;
import org.example.electricstore.service.impl.BrandService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/Admin/brand-manager")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public ModelAndView showListBrand(
            Authentication authentication,
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {

        ModelAndView modelAndView = new ModelAndView("admin/product_brand_category/listBrand");
        String username = authentication.getName();
        Page<Brand> brandPage;

        String filterKeyword = keyword.trim();
        if (!filterKeyword.isEmpty()) {
            brandPage = brandService.findByNameContainingPaginated(filterKeyword, page, size);
        } else {
            brandPage = brandService.getAllBrandsPaginated(page, size);
        }

        if (page >= brandPage.getTotalPages() && brandPage.getTotalPages() > 0) {
            int lastPage = Math.max(0, brandPage.getTotalPages() - 1);
            ModelAndView redirectView = new ModelAndView("redirect:/Admin/brand-manager");
            redirectView.addObject("page", lastPage);
            if (!filterKeyword.isEmpty()) {
                redirectView.addObject("keyword", filterKeyword);
            }
            return redirectView;
        }

        modelAndView.addObject("brands", brandPage.getContent());
        modelAndView.addObject("currentPage", page + 1); // Hiển thị trang từ 1 (UI), nhưng trang thực tế từ 0 (backend)
        modelAndView.addObject("totalPages", brandPage.getTotalPages() > 0 ? brandPage.getTotalPages() : 1);
        modelAndView.addObject("keyword", filterKeyword);
        modelAndView.addObject("brand", new BrandDTO());
        modelAndView.addObject("username", username);

        return modelAndView;
    }

    @PostMapping("/add")
    public String addBrand(@Valid @ModelAttribute("brand") BrandDTO brandDTO,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            // Vẫn giữ cách xử lý lỗi của form thông thường
            redirectAttributes.addFlashAttribute("errorMessage", "Dữ liệu nhập không hợp lệ!");
            return "redirect:/Admin/brand-manager";
        }

        Brand brand = new Brand();
        brand.setName(brandDTO.getName());
        if (brandDTO.getCountry() != null) {
            brand.setCountry(brandDTO.getCountry());
        }

        brandService.saveBrand(brand);

        redirectAttributes.addFlashAttribute("successMessage", "Thêm thương hiệu thành công!");
        return "redirect:/Admin/brand-manager";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditBrandForm(@PathVariable("id") Integer id) {
        ModelAndView modelAndView = new ModelAndView("admin/product_brand_category/editBrand");

        Optional<Brand> brandOptional = brandService.getBrandById(id);
        if (brandOptional.isPresent()) {
            modelAndView.addObject("brand", brandOptional.get());
        } else {
            modelAndView.setViewName("redirect:/Admin/brand-manager");
        }

        return modelAndView;
    }

    @PostMapping("/edit")
    public String updateBrand(@Valid @ModelAttribute("brand") Brand brand,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Dữ liệu cập nhật không hợp lệ!");
            return "redirect:/Admin/brand-manager";
        }

        brand.setUpdateAt(LocalDateTime.now());
        brandService.saveBrand(brand);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thương hiệu thành công!");
        return "redirect:/Admin/brand-manager";
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> deleteBrands(@RequestBody List<Integer> brandIds) {
        try {
            brandService.deleteBrand(brandIds);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Thương hiệu đã được xóa thành công!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi khi xóa thương hiệu!");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/check-name")
    @ResponseBody
    public ResponseEntity<?> checkBrandNameExists(@RequestParam("name") String name) {
        boolean exists = brandService.existsByName(name);
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
}