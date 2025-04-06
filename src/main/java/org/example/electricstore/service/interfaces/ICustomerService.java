package org.example.electricstore.service.interfaces;

import org.example.electricstore.DTO.customer.CustomerDTO;
import org.example.electricstore.model.Customer;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ICustomerService<E,D>{
    Page<E> getAllCustomers(int page, int size);
    Page<E> searchByFieldAndKey(String field, String keyword , int page, int size);
    E getCustomerById(int id);
    void saveCustomer(D d);
    void updateCustomer(D d , int id);
    D findCustomerDTOById(int id);
    List<CustomerDTO> getCustomersByKeyword(String keyword);
    Optional<Customer> getCustomerByCustomerId(Integer customerId);

    Page<Customer> searchCustomers(String keyword, String filter, Integer page, Integer size);
}
