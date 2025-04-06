package org.example.electricstore.service.interfaces;

import org.example.electricstore.DTO.customer.CustomerDTO;
import org.example.electricstore.DTO.order.OrderDTO;
import org.example.electricstore.DTO.order.OrderDetailDTO;
import org.example.electricstore.DTO.order.OrderHistoryRq;
import org.example.electricstore.model.Customer;
import org.example.electricstore.model.Order;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderService {
    //    Page<CustomerDTO> searchCustomers(String keyword, String filter, Integer page, Integer size);
    List<Order> getCompletedOrders(LocalDateTime startDate, LocalDateTime endDate);
    long getTotalCompletedOrders(LocalDateTime startDate, LocalDateTime endDate);
    double getTotalRevenue(LocalDateTime startDate, LocalDateTime endDate);

    Integer saveOrder(OrderDTO orderDTO);
    Page<CustomerDTO> getAllCustomersDTO(Integer page, Integer size);
//    Page<CustomerDTO> searchCustomers(String keyword, String filter, Integer page, Integer size);
    OrderDTO getOrderDTOById(Integer orderId);
    Order getOrderById(Integer id);
    List<OrderHistoryRq> getAllOrderHistoryRqByCustomer(Customer customer);
    List<OrderDetailDTO> getAllOrderDetailDTOByCustomer(int orderId);

}
