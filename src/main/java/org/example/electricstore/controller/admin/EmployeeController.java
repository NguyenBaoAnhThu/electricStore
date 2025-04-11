package org.example.electricstore.controller.admin;

import org.example.electricstore.DTO.employee.EmployeeDTO;
import org.example.electricstore.model.Employee;
import org.example.electricstore.model.EmployeePosition;
import org.example.electricstore.repository.IUserRepository;
import org.example.electricstore.service.impl.EmployeePositionService;
import org.example.electricstore.service.interfaces.IEmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Admin")
public class EmployeeController {

    private final IEmployeeService employeeService;
    private final EmployeePositionService employeePositionService;
    private final IUserRepository userRepository;
    public EmployeeController(IEmployeeService employeeService,
                              EmployeePositionService employeePositionService ,
                              IUserRepository userRepository) {
        this.employeeService = employeeService;
        this.employeePositionService = employeePositionService;
        this.userRepository = userRepository;
    }

    @ModelAttribute("employeePositions")
    public List<EmployeePosition> getEmployeePositions() {
        return this.employeePositionService.getEmployeePositions();
    }

    @GetMapping("/employee-manager")
    public ModelAndView employeeList(@RequestParam(name = "searchField", required = false) String field,
                                     @RequestParam(name = "searchInput",
                                             required = false,
                                             defaultValue = "") String keyword,
                                     @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                     @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        ModelAndView modelAndView = new ModelAndView("admin/employee/listEmployee");

        Page<Employee> employeesPage = this.employeeService.findAll(page, size);

        String filterKeyWord = keyword.trim();
        if (!filterKeyWord.isEmpty() && field != null) {
            employeesPage = this.employeeService.searchByFieldAndKeyword(field, filterKeyWord, page, size);
        }

        if (employeesPage.isEmpty()) {
            modelAndView.addObject("message", "Không có nhân viên phù hợp với dữ liệu.");
        }

        modelAndView.addObject("employees", employeesPage);
        modelAndView.addObject("field", field);
        modelAndView.addObject("filterKeyWord", filterKeyWord);
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("totalPages", employeesPage.getTotalPages());
        modelAndView.addObject("employeeDTO", new EmployeeDTO());
        modelAndView.addObject("employeePosition", new EmployeePosition());

        return modelAndView;
    }

    @PostMapping("/employee-manager/create")
    public ResponseEntity<?> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO, BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            if (this.userRepository.existsByEmail(employeeDTO.getEmail())) {
                errors.put("email", "Email đã tồn tại");
            }

            if (this.employeeService.existedByPhone(employeeDTO.getEmployeePhone())) {
                errors.put("employeePhone", "Số điện thoại đã tồn tại");
            }
            if (this.userRepository.existsByUsername(employeeDTO.getUsername())) {
                errors.put("username" , "Tên đăng nhập đã tồn tại !") ;
            }

            if (!errors.isEmpty()) {
                errors.put("globalError", "Email hoặc số điện thoại đã tồn tại. Vui lòng nhập lại!");
                return ResponseEntity.badRequest().body(errors);
            }

            this.employeeService.save(employeeDTO);
            return ResponseEntity.ok("Thêm nhân viên thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("globalError", "Đã có lỗi xảy ra, vui lòng thử lại!"));
        }
    }

    @PostMapping("/employee-manager/disable")
    public ResponseEntity<?> toggleEmployeeStatus(@RequestBody Map<String, List<Integer>> request) {
        List<Integer> employeeIds = request.get("employeeIds");

        if (employeeIds == null || employeeIds.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Không có nhân viên nào được chọn"));
        }

        try {
            List<Employee> employees = employeeService.findByIds(employeeIds);
            for (Employee emp : employees) {
                emp.setIsDisabled(!emp.getIsDisabled()); // Toggle status
            }
            employeeService.saveAll(employees);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cập nhật trạng thái nhân viên thành công");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Lỗi: " + e.getMessage()));
        }
    }

    @PostMapping("/employee-position/create-form")
    public String createEmployeePosition(@ModelAttribute("employeePosition") EmployeePosition employeePosition,
                                         BindingResult bindingResult,
                                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("positionErrors", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("showPositionModal", true);
            return "redirect:/Admin/employee-manager";
        }

        try {
            employeePositionService.save(employeePosition);
            redirectAttributes.addFlashAttribute("message", "Thêm chức vụ thành công");
            redirectAttributes.addFlashAttribute("showSuccessAlert", true); // Add this line to trigger success alert
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi thêm chức vụ: " + e.getMessage());
            redirectAttributes.addFlashAttribute("showErrorAlert", true); // Add this line to trigger error alert
        }
        return "redirect:/Admin/employee-manager";
    }

    @GetMapping("/employee-position/get-all")
    @ResponseBody
    public ResponseEntity<List<EmployeePosition>> getAllPositions() {
        List<EmployeePosition> positions = employeePositionService.getEmployeePositions();
        return ResponseEntity.ok(positions);
    }
    @PostMapping("/employee-position/delete/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteEmployeePosition(@PathVariable("id") Integer positionId) {
        Map<String, Object> response = new HashMap<>();

        try {
            employeePositionService.deleteById(positionId);
            response.put("success", true);
            response.put("message", "Xóa chức vụ thành công");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi xóa chức vụ: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}