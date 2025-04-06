package org.example.electricstore.exception;


import org.example.electricstore.DTO.customer.CustomerDTO;
import org.example.electricstore.exception.customer.CustomerException;
import org.example.electricstore.service.impl.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final CustomerService customerService;
    public GlobalExceptionHandler(CustomerService customerService) {
        this.customerService = customerService;
    }
    @ExceptionHandler(NumberFormatException.class)
    public ModelAndView handleNumberFormatException(HttpServletRequest req) {
        ModelAndView modelAndView = new ModelAndView("admin/customer/listCustomer");
        String field = req.getParameter("searchField");
        String key = req.getParameter("searchInput");
        modelAndView.addObject("formatError",
                "Dữ liệu \"" + key + "\" không đúng định dạng \"" + field.toUpperCase() + "\" !");
        modelAndView.addObject("field" , field);
        modelAndView.addObject("filterKeyWord" , key);
        modelAndView.addObject("customers" ,this.customerService.getAllCustomers(1 , 5));
        return modelAndView;
    }

    @ExceptionHandler(CustomerException.class)
    public ModelAndView handleCustomerException(HttpServletRequest request, CustomerException ex) {
        ModelAndView modelAndView;
        Integer id = request.getParameter("customerId") != null
                ? Integer.parseInt(request.getParameter("customerId"))
                : null;

        CustomerDTO customerDTO = CustomerDTO.builder()
                .customerId(id)
                .customerName(request.getParameter("customerName"))
                .phoneNumber(request.getParameter("phoneNumber"))
                .address(request.getParameter("address"))
                .birthDate(LocalDate.parse(request.getParameter("birthDate")))
                .build();
        if (request.getRequestURI().startsWith("/Admin/customers/update")) {
            modelAndView = new ModelAndView("admin/customer/editCustomer");
        } else {
            modelAndView = new ModelAndView("admin/customer/listCustomer");
        }
        modelAndView.addObject("customerDTO" , customerDTO) ;
        modelAndView.addObject("error", ex.getMessage());
        return modelAndView;
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex, Model model, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        String errorType = ex.getClass().getSimpleName();

        model.addAttribute("error", errorType);
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("trace", ex.getStackTrace());
        model.addAttribute("status", status.value());
        model.addAttribute("path", request.getRequestURI());
        model.addAttribute("timestamp", LocalDateTime.now());

        return "error";
    }
}
