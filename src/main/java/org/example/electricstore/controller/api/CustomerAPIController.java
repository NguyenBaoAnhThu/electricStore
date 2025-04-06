package org.example.electricstore.controller.api;

import org.example.electricstore.DTO.customer.CustomerDTO;
import org.example.electricstore.service.impl.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerAPIController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/select/{keyword}")
    public List<CustomerDTO> getCustomersByKeyword(@PathVariable String keyword) {
        return this.customerService.getCustomersByKeyword(keyword);
    }

}
