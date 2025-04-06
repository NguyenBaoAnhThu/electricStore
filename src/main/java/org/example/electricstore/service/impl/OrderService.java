package org.example.electricstore.service.impl;

import org.example.electricstore.DTO.customer.CustomerDTO;
import org.example.electricstore.DTO.order.*;
import org.example.electricstore.enums.OrderStatus;
import org.example.electricstore.mapper.order.OrderMapper;
import org.example.electricstore.model.Customer;
import org.example.electricstore.model.Order;
import org.example.electricstore.model.OrderDetail;
import org.example.electricstore.repository.ICustomerRepository;
import org.example.electricstore.repository.IOrderDetailRepository;
import org.example.electricstore.repository.IOrderRepository;
import org.example.electricstore.repository.IProductRepository;
import org.example.electricstore.service.common.PDFService;
import org.example.electricstore.service.interfaces.IOrderService;
import org.example.electricstore.service.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService implements IOrderService {

    @Autowired
    CustomerService customerService;
//Sau move to customer
    @Autowired
    ICustomerRepository customerRepository;

    @Autowired
    IOrderRepository orderRepository;

    @Autowired
    IOrderDetailRepository orderDetailRepository;

    @Autowired
    IProductService productService;

    @Autowired
    IProductRepository productRepository;
  
    @Autowired
    OrderMapper orderMapper;


    @Autowired
    PDFService pdfService;

    @Override
    public Integer saveOrder(OrderDTO orderDTO) {
        System.out.println("orderDTO: " + orderDTO);
        Order order= new Order();
        Integer totalPrice = 0;

        //customer
        Integer customerID = orderDTO.getCustomerId();
        Customer customer = customerService.getCustomerById(customerID);
        order.setCustomer(customer);

        order.setTotalPrice(0.00);
        if(orderDTO.getPaymentMethod() == 1) {
            order.setStatus(OrderStatus.PENDING);
        } else {
            order.setStatus(OrderStatus.DELIVERED);
        }

        order.setCreateAt(LocalDateTime.now());
        order.setUpdateAt(LocalDateTime.now());
        Order saveOrder = orderRepository.save(order);

        Integer orderID = saveOrder.getOrderID();

        //orderDetails
        for ( ProductOrderDTO productOrderDTO : orderDTO.getProductOrderDTOList()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(saveOrder);
            orderDetail.setProduct(productService.findById(productOrderDTO.getProductId()));
            orderDetail.setQuantity(productOrderDTO.getQuantity());
            orderDetail.setPrice((double)productOrderDTO.getPriceIndex());

            saveOrderDetail(orderDetail);
            totalPrice += productOrderDTO.getQuantity() * productOrderDTO.getPriceIndex();

        }

        saveOrder.setTotalPrice((double)totalPrice);

        Double totalPrice_d  = (double)totalPrice;

        orderRepository.updateTotalPrice(orderID, totalPrice_d);

        return orderID;

    }

    public void saveOrderDetail(OrderDetail orderDetail) {
        orderDetailRepository.save(orderDetail);
    }


    @Override
    public Page<CustomerDTO> getAllCustomersDTO(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Customer> customers = customerRepository.findAll(pageable);
        return customers.map(this::convertToDTO);
    }


    @Override
    public Order getOrderById(Integer id) {
        return this.orderRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public List<OrderHistoryRq> getAllOrderHistoryRqByCustomer(Customer customer) {
        return this.orderRepository.findByCustomer(customer).stream().map(
                order -> this.orderMapper.toOrderHistoryRq(order)
        ).toList();
    }
    @Transactional
    @Override
    public List<OrderDetailDTO> getAllOrderDetailDTOByCustomer(int orderId) {
        Order order = this.orderRepository.findById(orderId).orElseThrow(
                () -> new RuntimeException("Order not found"));
        return order.getOrderDetails().stream().map(
                orderDetail -> this.orderMapper.toOrderDetailDTO(orderDetail)
        ).toList();
    }


    private CustomerDTO convertToDTO(Customer customer) {
        return new CustomerDTO(
                customer.getCustomerId(),
                customer.getCustomerName(),
                customer.getPhoneNumber(),
                customer.getAddress(),
                customer.getBirthDate(),
                customer.getEmail()
        );
    }
    @Override
    public List<Order> getCompletedOrders(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByStatusAndCreateAtBetween(OrderStatus.DELIVERED, startDate, endDate);
    }

    @Override
    public long getTotalCompletedOrders(LocalDateTime startDate, LocalDateTime endDate) {
        return getCompletedOrders(startDate, endDate).size();
    }

    @Override

    public double getTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        return getCompletedOrders(startDate, endDate)
                .stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();
    }

    @Transactional
    public OrderDTO getOrderDTOById(Integer orderId) {

        OrderDTO orderDTO = new OrderDTO();
        Order order = orderRepository.findById(orderId).orElse(null);
        System.out.println("order: " + order);

        if (order == null) {
            return null;
        }
        CustomerDTO customerDTO = new CustomerDTO(
                order.getCustomer().getCustomerId(),
                order.getCustomer().getCustomerName(),
                order.getCustomer().getPhoneNumber(),
                order.getCustomer().getAddress(),
                order.getCustomer().getBirthDate(),
                order.getCustomer().getEmail()
        );


        orderDTO.setId(order.getOrderID());
        orderDTO.setCustomerDTO(customerDTO);

        //remove default productOrderDTO in productOrderDTOList
        orderDTO.getProductOrderDTOList().removeFirst();

        List<ProductOrderDTO> productOrderDTOList = order.getOrderDetails().stream().map(orderDetail -> {
            ProductOrderDTO productOrderDTO = new ProductOrderDTO();
            productOrderDTO.setProductId(orderDetail.getProduct().getProductID());
            productOrderDTO.setProductName(orderDetail.getProduct().getName());
            productOrderDTO.setPriceIndex(orderDetail.getPrice().intValue());
            productOrderDTO.setQuantity(orderDetail.getQuantity());
            return productOrderDTO;
        }).toList();

        for( ProductOrderDTO productOrderDTO : productOrderDTOList) {
            orderDTO.getProductOrderDTOList().add(productOrderDTO);
        }

        return orderDTO;
    }

}
