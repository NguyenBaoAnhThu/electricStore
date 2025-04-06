package org.example.electricstore.service.interfaces;

import org.example.electricstore.DTO.order.OrderDetailRevenueDTO;
import org.example.electricstore.DTO.product.ProductStatisticalDTO;
import org.example.electricstore.DTO.statistical.DailyRevenueDTO;
import org.example.electricstore.DTO.statistical.RevenueDetailDTO;
import org.example.electricstore.DTO.statistical.RevenueSummaryDTO;
import org.example.electricstore.DTO.statistical.TopSellingProductDTO;
import org.example.electricstore.model.Order;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;

public interface IStatisticalService {
    RevenueSummaryDTO getRevenueSummary(List<Order> orderList);
    List<DailyRevenueDTO> getDailyRevenue(List<Order> orderList , String type);
    List<TopSellingProductDTO> getTopSellingProduct(List<Order> orderList);
    List<Order> getListOrdersByDate(int day , int month , int year);
    List<Order> getAllOrders() ;
    Page<OrderDetailRevenueDTO> getOrderDetailRevenue(List<Order> orderList , int page , int size);
    Page<ProductStatisticalDTO> getProductStatistical(List<Order> orderList, int page , int size);
    Page<RevenueDetailDTO> getRevenueDetail(List<Order> orderList , int page , int size);
    List<RevenueDetailDTO> getAllRevenueDetail(List<Order> orderList );
    Integer getTotalProductsSales(List<Order> orderList);
    Integer getStockProducts (List<Order> orderList);
    HashMap<String , Double> getTotalDetailRevenue(List<RevenueDetailDTO> revenueDetailDTOS);
}
