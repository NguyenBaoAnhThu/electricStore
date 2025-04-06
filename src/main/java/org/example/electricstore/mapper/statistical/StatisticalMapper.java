package org.example.electricstore.mapper.statistical;

import org.example.electricstore.DTO.order.OrderDetailRevenueDTO;
import org.example.electricstore.DTO.statistical.DailyRevenueDTO;
import org.example.electricstore.DTO.statistical.RevenueDetailDTO;
import org.example.electricstore.DTO.statistical.RevenueSummaryDTO;
import org.example.electricstore.enums.OrderStatus;
import org.example.electricstore.model.Order;
import org.example.electricstore.model.OrderDetail;
import org.example.electricstore.model.Product;
import org.example.electricstore.model.WareHouse;
import org.example.electricstore.repository.IWareHouseRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
@Transactional
public class StatisticalMapper {

    private final IWareHouseRepository wareHouseRepository;
    public StatisticalMapper(IWareHouseRepository wareHouseRepository) {
        this.wareHouseRepository = wareHouseRepository;
    }

    public RevenueSummaryDTO convertToRevenueSummaryDTO (List<Order> orderList) {
        int totalOrdersSuccess = 0;
        int totalOrdersFail = 0;
        int totalItems = 0;
        double totalRevenue = 0.0;

       for (Order order : orderList) {
           if (order.getStatus().equals(OrderStatus.DELIVERED)) {
               totalOrdersSuccess++ ;
           } else if (order.getStatus().equals(OrderStatus.CANCELLED)) {
               totalOrdersFail++ ;
           }
           totalRevenue += order.getTotalPrice() ;

           for (OrderDetail orderDetail : order.getOrderDetails()) {
               totalItems += orderDetail.getQuantity();
           }
       }
       return RevenueSummaryDTO.builder()
               .totalOrdersSuccess(totalOrdersSuccess)
               .totalOrdersFailed(totalOrdersFail)
               .totalItems(totalItems)
               .revenue(totalRevenue)
               .build();
    }

        public List<DailyRevenueDTO> convertToDailyRevenueDTO(List<Order> orderList, String type) {
            return orderList.stream()
                    .collect(Collectors.groupingBy(
                            order -> {
                                LocalDate date = order.getCreateAt().toLocalDate();
                                if ("month".equalsIgnoreCase(type)) {
                                    return LocalDate.of(date.getYear(), date.getMonthValue(), 1);
                                } else if ("year".equalsIgnoreCase(type)) {
                                    return LocalDate.of(date.getYear(), 1, 1);
                                } else {
                                    return date;
                                }
                            },
                            Collectors.summingDouble(Order::getTotalPrice)
                    ))
                    .entrySet().stream()
                    .map(entry -> {
                        LocalDate date = entry.getKey();

                        Integer day = "day".equalsIgnoreCase(type) ? date.getDayOfMonth() : null;
                        int month = ("day".equalsIgnoreCase(type) || "month".equalsIgnoreCase(type)) ? date.getMonthValue() : 0;
                        int year = date.getYear();

                        return DailyRevenueDTO.builder()
                                .day(day)
                                .month(month)
                                .year(year)
                                .revenue(entry.getValue())
                                .build();
                    })
                    .sorted(Comparator.comparing(DailyRevenueDTO::getYear)
                            .thenComparing(dto -> dto.getMonth() != 0 ? dto.getMonth() : Integer.MAX_VALUE)
                            .thenComparing(dto -> dto.getDay() != null ? dto.getDay() : Integer.MAX_VALUE))
                    .collect(Collectors.toList());
        }

    public OrderDetailRevenueDTO orderDetailRevenueDTO(Order order) {
        int quantity = 0;
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            quantity += orderDetail.getQuantity();
        }
        return OrderDetailRevenueDTO.builder()
                .code(order.getCodeOrder())
                .customerName(order.getCustomer().getCustomerName())
                .orderDate(order.getCreateAt())
                .quantity(quantity)
                .price(order.getTotalPrice())
                .status(order.getStatus().name())
                .build();
    }
    public List<RevenueDetailDTO> convertToRevenueDetailDTO(List<Order> orderList) {

        Map<Integer, Double> importPriceMap = this.wareHouseRepository.findAll().stream()
                .collect(Collectors.toMap(
                        wareHouse -> wareHouse.getProduct().getProductID(),
                        WareHouse::getPrice
                ));

        return orderList.stream()
                .flatMap(order -> order.getOrderDetails().stream())
                .map(orderDetail -> {
                    Product product = orderDetail.getProduct();
                    Integer productId = product.getProductID();
                    Integer quantitySold = orderDetail.getQuantity();
                    Double sellingPrice = product.getPrice();
                    Double importPrice = importPriceMap.get(productId);

                    return RevenueDetailDTO.builder()
                            .id(productId)
                            .productName(product.getName())
                            .quantitySold(quantitySold)
                            .sellingPrice(sellingPrice)
                            .importPrice(importPrice)
                            .totalSellingPrice(quantitySold * sellingPrice)
                            .totalImportCost(quantitySold * importPrice)
                            .profit((sellingPrice - importPrice) * quantitySold)
                            .profitRate(importPrice > 0 ? ((sellingPrice - importPrice) / importPrice) * 100 : 0.0)
                            .build();
                })
                .collect(Collectors.toList());
    }




}
