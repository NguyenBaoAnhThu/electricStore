package org.example.electricstore.service.impl;

import org.example.electricstore.DTO.order.OrderDetailRevenueDTO;
import org.example.electricstore.DTO.product.ProductStatisticalDTO;
import org.example.electricstore.DTO.statistical.DailyRevenueDTO;
import org.example.electricstore.DTO.statistical.RevenueDetailDTO;
import org.example.electricstore.DTO.statistical.RevenueSummaryDTO;
import org.example.electricstore.DTO.statistical.TopSellingProductDTO;
import org.example.electricstore.mapper.statistical.StatisticalMapper;
import org.example.electricstore.model.Order;
import org.example.electricstore.model.OrderDetail;
import org.example.electricstore.repository.IRevenueRepository;
import org.example.electricstore.service.interfaces.IStatisticalService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;


@Service
@Transactional
public class StatisticalService implements IStatisticalService {

    private final IRevenueRepository irevenueRepository;
    private final StatisticalMapper statisticalMapper;

    public StatisticalService(IRevenueRepository repository, StatisticalMapper mapper) {
        this.irevenueRepository = repository;
        this.statisticalMapper = mapper;
    }

    @Override
    public RevenueSummaryDTO getRevenueSummary(List<Order> orderList) {
        return this.statisticalMapper.convertToRevenueSummaryDTO(orderList);
    }
    @Override
    public List<DailyRevenueDTO> getDailyRevenue(List<Order> orderList , String type) {
        return this.statisticalMapper.convertToDailyRevenueDTO(orderList , type);
    }

    @Override
    public List<TopSellingProductDTO> getTopSellingProduct(List<Order> orderList) {
        List<Integer> integerList = orderList.stream().map(Order::getOrderID).toList();
        return this.irevenueRepository.findTopSellingProducts(integerList);
    }

    @Override
    public List<Order> getListOrdersByDate(int day, int month, int year) {
        LocalDateTime startDate;
        LocalDateTime endDate;

        if (day > 0) {
            LocalDate date = LocalDate.of(year, month, day);
            startDate = date.atStartOfDay();
            endDate = date.atTime(LocalTime.MAX);
        } else if (month > 0) {
            LocalDate date = LocalDate.of(year, month, 1);
            startDate = date.atStartOfDay();
            endDate = date.plusMonths(1).atStartOfDay();
        } else {
            LocalDate date = LocalDate.of(year, 1, 1);
            startDate = date.atStartOfDay();
            endDate = date.plusYears(1).minusDays(1).atTime(LocalTime.MAX);
        }
        return this.irevenueRepository.findAllByOrderByCreateAt(startDate, endDate);
    }

    @Override
    public List<Order> getAllOrders() {
        return this.irevenueRepository.findAllOrders();
    }
    @Override
    public Page<OrderDetailRevenueDTO> getOrderDetailRevenue(List<Order> orderList, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size );

        List<OrderDetailRevenueDTO> orderDetailRevenueDTOList = orderList.stream()
                .map(this.statisticalMapper::orderDetailRevenueDTO)
                .toList();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), orderDetailRevenueDTOList.size());
        List<OrderDetailRevenueDTO> pagedList = orderDetailRevenueDTOList.subList(start, end);
        return new PageImpl<>(pagedList, pageable, orderDetailRevenueDTOList.size());
    }


    @Override
    public Page<ProductStatisticalDTO> getProductStatistical(List<Order> orderList, int page, int size) {
        List<Integer> integerList = orderList.stream().map(Order::getOrderID).toList();
        Pageable pageable = PageRequest.of(page - 1, size);

        List<ProductStatisticalDTO> productStatisticalDTOS = this.irevenueRepository.findProductsSales(integerList);

        List<ProductStatisticalDTO> sortedList = productStatisticalDTOS.stream()
                .sorted(Comparator.comparing(ProductStatisticalDTO::getStock).reversed())
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), sortedList.size());
        List<ProductStatisticalDTO> pagedList = sortedList.subList(start, end);

        return new PageImpl<>(pagedList, pageable, sortedList.size());
    }


    @Override
    public Page<RevenueDetailDTO> getRevenueDetail(List<Order> orderList, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        List<RevenueDetailDTO> revenueDetailDTOS = this.statisticalMapper.convertToRevenueDetailDTO(orderList);

        Map<Integer, RevenueDetailDTO> revenueMap = new HashMap<>();
        for (RevenueDetailDTO dto : revenueDetailDTOS) {
            revenueMap.merge(dto.getId(), dto, (existing, newDto) -> {
                existing.setQuantitySold(existing.getQuantitySold() + newDto.getQuantitySold());
                existing.setTotalImportCost(existing.getTotalImportCost() + newDto.getTotalImportCost());
                existing.setTotalSellingPrice(existing.getTotalSellingPrice() + newDto.getTotalSellingPrice());
                existing.setProfit(existing.getProfit() + newDto.getProfit());

                if (existing.getTotalImportCost() > 0) {
                    double profitRate = (existing.getProfit() / existing.getTotalImportCost()) * 100;
                    existing.setProfitRate(profitRate);
                } else {
                    existing.setProfitRate(0.0);
                }
                return existing;
            });
        }

        List<RevenueDetailDTO> sortedList = new ArrayList<>(revenueMap.values());
        sortedList.sort(Comparator.comparing(RevenueDetailDTO::getQuantitySold).reversed());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), sortedList.size());
        List<RevenueDetailDTO> pagedList = sortedList.subList(start, end);

        return new PageImpl<>(pagedList, pageable, sortedList.size());
    }



    @Override
    public List<RevenueDetailDTO> getAllRevenueDetail(List<Order> orderList) {
        return this.statisticalMapper.convertToRevenueDetailDTO(orderList);
    }


    @Override
    public Integer getTotalProductsSales(List<Order> orderList) {
        return orderList.stream()
                .flatMap(order -> order.getOrderDetails().stream())
                .mapToInt(OrderDetail::getQuantity)
                .sum();
    }

    @Override
    public Integer getStockProducts(List<Order> orderList) {
        return (int) orderList.stream().flatMap(order -> order.getOrderDetails().stream())
                .map(orderDetail -> orderDetail.getProduct().getProductID())
                .distinct()
                .count();
    }
    @Override
    public HashMap<String, Double> getTotalDetailRevenue(List<RevenueDetailDTO> revenueDetailDTOS) {

        HashMap<String, Double> map = new HashMap<>();
        map.put("totalSellingPrice" , revenueDetailDTOS.stream().mapToDouble(RevenueDetailDTO::getTotalSellingPrice).sum()) ;
        map.put("totalImportCost" , revenueDetailDTOS.stream().mapToDouble(RevenueDetailDTO::getTotalImportCost).sum()) ;
        map.put("profit" , revenueDetailDTOS.stream().mapToDouble(RevenueDetailDTO::getProfit).sum()) ;
        map.put("profitRate" , revenueDetailDTOS.stream().mapToDouble(RevenueDetailDTO::getProfitRate).sum()) ;
        return map;
    }


}
