package org.example.electricstore.controller.report;


import org.example.electricstore.DTO.report.CustomerReportDTO;
import org.example.electricstore.service.impl.CustomerReportService;
import org.example.electricstore.service.interfaces.ICustomerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;

@Controller
@RequestMapping("/Admin/report")
public class CustomerReportController {

    @Autowired
    private CustomerReportService customerReportService;

    @Autowired
    private ICustomerService customerService;

    @GetMapping
    public String listReports(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerReportDTO> reportPage;
        long totalCustomers;

        if (fromDate != null && toDate != null) {
            reportPage = customerReportService.getCustomerReportsByDateRange(fromDate, toDate, pageable);
            totalCustomers = customerReportService.countTotalCustomersByDateRange(fromDate, toDate);
        } else {
            LocalDate defaultFrom = LocalDate.now().minusMonths(1);
            LocalDate defaultTo = LocalDate.now();
            reportPage = customerReportService.getCustomerReportsByDateRange(defaultFrom, defaultTo, pageable);
            totalCustomers = customerReportService.countTotalCustomersByDateRange(defaultFrom, defaultTo);
        }

        model.addAttribute("customerReports", reportPage);
        model.addAttribute("totalCustomers", totalCustomers);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reportPage.getTotalPages());

        return "admin/customer_report/customer_report_list";
    }

    @PostMapping("/addRange")
    public String updateReportsByDateRange(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (fromDate.isAfter(toDate)) {
            redirectAttributes.addFlashAttribute("message", "❌ Ngày bắt đầu không thể lớn hơn ngày kết thúc.");
            return "redirect:/Admin/report";
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerReportDTO> reportPage = customerReportService.getCustomerReportsByDateRange(fromDate, toDate, pageable);
        long totalCustomers = customerReportService.countTotalCustomersByDateRange(fromDate, toDate);

        model.addAttribute("customerReports", reportPage);
        model.addAttribute("totalCustomers", totalCustomers);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", reportPage.getTotalPages());

        redirectAttributes.addFlashAttribute("message",
                "✅ Lấy " + reportPage.getNumberOfElements() + " báo cáo, tổng " + totalCustomers + " khách hàng từ " + fromDate + " đến " + toDate + " thành công!");

        return "admin/customer_report/customer_report_list";
    }

    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) throws IOException {
        customerReportService.exportCustomerReportsToExcel(response, fromDate, toDate);
    }
}