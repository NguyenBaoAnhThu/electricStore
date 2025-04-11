package org.example.electricstore.controller.admin;

import org.example.electricstore.DTO.product.ProductStatisticalDTO;
import org.example.electricstore.DTO.statistical.*;
import org.example.electricstore.model.Order;
import org.example.electricstore.service.impl.StatisticalService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/Admin/statistical")
public class StatisticalController {

    private final StatisticalService statisticalService;

    public StatisticalController(StatisticalService statisticalService) {
        this.statisticalService = statisticalService;
    }


    @GetMapping
    public String showOverView(@RequestParam(required = false) String type) {
        switch (type.toLowerCase()) {
            case "day":
                return "admin/statistical/sales_report_day";
            case "month":
                return "admin/statistical/sales_report_month";
            case "year":
                return "admin/statistical/sales_report_year";
        }
        return "admin/statistical/sales_report_day";

    }

    @GetMapping("/api/revenue")
    @ResponseBody
    public ResponseEntity<OverViewRevenueDTO> getRevenueSummaryByDay(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer day,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {

        LocalDate today = LocalDate.now();
        int selectedDay = (day != null) ? day : today.getDayOfMonth();
        int selectedMonth = (month != null) ? month : today.getMonthValue();
        int selectedYear = (year != null) ? year : today.getYear();


        RevenueSummaryDTO revenueSummaryDTO = null;
        List<DailyRevenueDTO> dailyRevenueDTOList = new ArrayList<>();
        List<TopSellingProductDTO> topSellingProductDTOList = new ArrayList<>();

        switch (type.toLowerCase()) {
            case "day":
                revenueSummaryDTO = this.statisticalService.getRevenueSummary(
                        this.statisticalService.getListOrdersByDate(selectedDay, selectedMonth, selectedYear));
                dailyRevenueDTOList = this.statisticalService.getDailyRevenue(
                        this.statisticalService.getListOrdersByDate(0, selectedMonth, selectedYear), "day");
                topSellingProductDTOList = this.statisticalService.getTopSellingProduct(
                        this.statisticalService.getListOrdersByDate(0, selectedMonth, selectedYear));
                break;
            case "month":
                revenueSummaryDTO = this.statisticalService.getRevenueSummary(
                        this.statisticalService.getListOrdersByDate(0, selectedMonth, selectedYear));
                dailyRevenueDTOList = this.statisticalService.getDailyRevenue(
                        this.statisticalService.getListOrdersByDate(0, 0, selectedYear), "month");
                topSellingProductDTOList = this.statisticalService.getTopSellingProduct(
                        this.statisticalService.getListOrdersByDate(0, 0, selectedYear));
                break;
            case "year":
                revenueSummaryDTO = this.statisticalService.getRevenueSummary(
                        this.statisticalService.getListOrdersByDate(0, 0, selectedYear));
                dailyRevenueDTOList = this.statisticalService.getDailyRevenue(
                        this.statisticalService.getAllOrders(), "year");
                topSellingProductDTOList = this.statisticalService.getTopSellingProduct(
                        this.statisticalService.getAllOrders());
                break;
        }

        return ResponseEntity.ok(OverViewRevenueDTO.builder()
                .revenueSummary(revenueSummaryDTO)
                .dailyRevenue(dailyRevenueDTOList)
                .topSellingProducts(topSellingProductDTOList)
                .build());
    }

    @GetMapping("/orders/detail")
    public String getOrdersDetail(@RequestParam(required = false) String type,
                                  @RequestParam(required = false) Integer day,
                                  @RequestParam(required = false) Integer month,
                                  @RequestParam(required = false) Integer year,
                                  @RequestParam(required = false, defaultValue = "1") Integer page,
                                  @RequestParam(required = false, defaultValue = "10") Integer size,
                                  @RequestParam(required = false) String status,
                                  Model model) {

        List<Order> orderList = new ArrayList<>();
        switch (type.toLowerCase()) {
            case "day":
                orderList = this.statisticalService.getListOrdersByDate(day, month, year);
                break;
            case "month":
                orderList = this.statisticalService.getListOrdersByDate(0, month, year);
                break;
            case "year":
                orderList = this.statisticalService.getListOrdersByDate(0, 0, year);
                break;
        }

        List<Order> filteredOrders = orderList.stream()
                .filter(order -> order.getStatus().name().equalsIgnoreCase(status))
                .toList();

        model.addAttribute("orders", this.statisticalService.getOrderDetailRevenue(filteredOrders, page, size));
        model.addAttribute("ordersSize", filteredOrders.size());
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("type", type);
        model.addAttribute("day", day);
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        model.addAttribute("status", status);

        return status.equalsIgnoreCase("DELIVERED")
                ? "admin/statistical/order-success-detail"
                : "admin/statistical/order-failure-detail";
    }


    @GetMapping("/products/detail")
    public String getProductsSalesDetail(@RequestParam(required = false) String type,
                                         @RequestParam(required = false) Integer day,
                                         @RequestParam(required = false) Integer month,
                                         @RequestParam(required = false) Integer year,
                                         @RequestParam(required = false, defaultValue = "1") Integer page,
                                         @RequestParam(required = false, defaultValue = "10") Integer size,
                                         Model model) {
        Page<ProductStatisticalDTO> productStatisticalDTOList = null;
        int totalProductsSales = 0 ;
        int totalStockProducts = 0 ;
        switch (type.toLowerCase()) {
            case "day":
                productStatisticalDTOList = this.statisticalService.getProductStatistical(
                        this.statisticalService.getListOrdersByDate(day, month, year), page, size);
                totalProductsSales = this.statisticalService.getTotalProductsSales
                        (this.statisticalService.getListOrdersByDate(day, month, year));
                totalStockProducts = this.statisticalService.getStockProducts
                        (this.statisticalService.getListOrdersByDate(day, month, year));
                break;
            case "month":
                productStatisticalDTOList = this.statisticalService.getProductStatistical(
                        this.statisticalService.getListOrdersByDate(0, month, year), page, size);
                totalProductsSales = this.statisticalService.getTotalProductsSales
                        (this.statisticalService.getListOrdersByDate(0, month, year));
                totalStockProducts = this.statisticalService.getStockProducts
                        (this.statisticalService.getListOrdersByDate(0, month, year));
                break;
            case "year":
                productStatisticalDTOList = this.statisticalService.getProductStatistical(
                        this.statisticalService.getListOrdersByDate(0, 0, year), page, size);
                totalProductsSales = this.statisticalService.getTotalProductsSales
                        (this.statisticalService.getListOrdersByDate(0, 0, year));
                totalStockProducts = this.statisticalService.getStockProducts
                        (this.statisticalService.getListOrdersByDate(0, 0, year));
                break;
        }
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("type", type);
        model.addAttribute("totalProductsSales", totalProductsSales);
        model.addAttribute("totalStockProducts", totalStockProducts);
        model.addAttribute("products", productStatisticalDTOList);
        model.addAttribute("day", day);
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        return "admin/statistical/products-detail";
    }

    @GetMapping("/revenue/detail")
    public String getRevenueDetail(@RequestParam(required = false) String type,
                                   @RequestParam(required = false) Integer day,
                                   @RequestParam(required = false) Integer month,
                                   @RequestParam(required = false) Integer year,
                                   @RequestParam(required = false, defaultValue = "1") Integer page,
                                   @RequestParam(required = false, defaultValue = "10   ") Integer size,
                                   Model model) {

        Page<RevenueDetailDTO> revenueDetailDTOS = null;
        List<RevenueDetailDTO> revenueDetailDTOList = null;
        List<Order> orders ;
        switch (type.toLowerCase()) {
            case "day":
                orders = this.statisticalService.getListOrdersByDate(day, month, year) ;
                revenueDetailDTOList = this.statisticalService.getAllRevenueDetail(orders) ;
                revenueDetailDTOS = this.statisticalService.getRevenueDetail
                        (orders, page, size);
                break;
            case "month":
                orders = this.statisticalService.getListOrdersByDate(0, month, year) ;
                revenueDetailDTOList = this.statisticalService.getAllRevenueDetail(orders) ;
                revenueDetailDTOS = this.statisticalService.getRevenueDetail
                        (orders, page, size);
                break;
            case "year":
                orders = this.statisticalService.getListOrdersByDate(0, 0, year) ;
                revenueDetailDTOList = this.statisticalService.getAllRevenueDetail(orders) ;
                revenueDetailDTOS = this.statisticalService.getRevenueDetail
                        (orders, page, size);
                break;
        }

        assert revenueDetailDTOList != null;
        HashMap<String , Double> map = this.statisticalService.getTotalDetailRevenue(revenueDetailDTOList) ;
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("type", type);
        model.addAttribute("totalRevenue", map.get("totalSellingPrice"));
        model.addAttribute("totalCost", map.get("totalImportCost"));
        model.addAttribute("netProfit", map.get("totalSellingPrice") - map.get("totalImportCost"));
        model.addAttribute("profitRate", map.get("profitRate") / revenueDetailDTOList.size());
        model.addAttribute("revenues", revenueDetailDTOS);
        model.addAttribute("day", day);
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        return "admin/statistical/revenue-detail";

    }


}