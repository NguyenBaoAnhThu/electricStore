package org.example.electricstore.controller.admin;


import org.example.electricstore.DTO.customer.CustomerDTO;
import org.example.electricstore.DTO.order.OrderDetailDTO;
import org.example.electricstore.DTO.order.OrderHistoryRq;
import org.example.electricstore.model.Customer;
import org.example.electricstore.service.impl.CustomerService;
import org.example.electricstore.service.impl.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Admin/customers")
public class CustomerController {


    private final CustomerService customerService;
    private final OrderService orderService;

    public CustomerController(CustomerService customerService ,
                              OrderService orderService) {
        this.customerService = customerService;
        this.orderService = orderService;
    }

    @GetMapping
    public ModelAndView getAndSearchCustomers(@RequestParam(name = "searchField", required = false) String field,
                                              @RequestParam(name = "searchInput",
                                                      required = false,
                                                      defaultValue = "") String keyword,
                                              @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                              @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        ModelAndView modelAndView = new ModelAndView("admin/customer/listCustomer");
        Page<Customer> customerPage;
        String filterKeyWord = keyword.trim();
        if (!filterKeyWord.isEmpty()) {
            customerPage = this.customerService.searchByFieldAndKey(field, filterKeyWord, page, size);
        } else {
            customerPage = this.customerService.getAllCustomers(page, size);
        }
        modelAndView.addObject("customers", customerPage);
        modelAndView.addObject("field", field);
        modelAndView.addObject("filterKeyWord", filterKeyWord);
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("totalPages", customerPage.getTotalPages());
        return modelAndView;
    }

    @GetMapping("/history/{id}")
    public ModelAndView getCustomerHistory(@PathVariable("id") Integer id) {
        ModelAndView modelAndView = new ModelAndView("admin/customer/historyCustomer");

        Customer customer = this.customerService.getCustomerById(id);
        List<OrderHistoryRq> orderHistoryRqs = this.orderService.getAllOrderHistoryRqByCustomer(customer);
        modelAndView.addObject("orderHistoryRqs", orderHistoryRqs);
        modelAndView.addObject("customer", customer);
        return modelAndView;
    }

    @GetMapping("/api/orders/{orderId}/details")
    @ResponseBody
    public List<OrderDetailDTO> getOrdersDetails(@PathVariable("orderId") Integer orderId) {
        return this.orderService.getAllOrderDetailDTOByCustomer(orderId) ;
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateCustomer(@Valid @RequestBody CustomerDTO customerDTO ,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        this.customerService.updateCustomer(customerDTO, customerDTO.getCustomerId());
        return ResponseEntity.ok("Đã cập nhật khách hàng thành công!");
    }
}
