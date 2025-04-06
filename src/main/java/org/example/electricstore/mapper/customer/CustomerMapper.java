package org.example.electricstore.mapper.customer;


import org.example.electricstore.DTO.customer.CustomerDTO;
import org.example.electricstore.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer convertToCustomer(CustomerDTO customerDTO) {
        return Customer.builder()
                .customerName(customerDTO.getCustomerName())
                .phoneNumber(customerDTO.getPhoneNumber())
                .address(customerDTO.getAddress())
                .birthDate(customerDTO.getBirthDate())
                .isDisabled(false)
                .build();
    }

    public CustomerDTO convertToCustomerDTO(Customer customer) {
        return CustomerDTO.builder()
                .customerId(customer.getCustomerId())
                .customerName(customer.getCustomerName())
                .phoneNumber(customer.getPhoneNumber())
                .address(customer.getAddress())
                .birthDate(customer.getBirthDate())
                .email(customer.getEmail())
                .build();
    }
}
