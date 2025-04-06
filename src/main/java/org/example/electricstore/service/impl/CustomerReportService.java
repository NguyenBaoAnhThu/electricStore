package org.example.electricstore.service.impl;


import org.example.electricstore.DTO.report.CustomerReportDTO;
import org.example.electricstore.enums.PaymentStatus;
import org.example.electricstore.enums.SpendingCategory;
import org.example.electricstore.repository.ICustomerReportRepository;
import org.example.electricstore.service.interfaces.ICustomerReportService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerReportService implements ICustomerReportService {

    @Autowired
    private ICustomerReportRepository customerReportRepository;

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public Page<CustomerReportDTO> getCustomerReportsByDateRange(LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        LocalDateTime startDate = fromDate.atStartOfDay();
        LocalDateTime endDate = toDate.atTime(23, 59, 59);

        Page<Object[]> summariesPage = customerReportRepository.getCompletedOrderSummary(startDate, endDate, pageable);
        if (summariesPage.isEmpty()) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        List<CustomerReportDTO> reports = new ArrayList<>();
        for (Object[] summary : summariesPage.getContent()) {
            Integer customerId = summary[0] != null ? (Integer) summary[0] : null;
            String customerName = summary[1] != null ? (String) summary[1] : "N/A";
            int totalOrders = summary[2] != null ? ((Number) summary[2]).intValue() : 0;
            double totalSpent = summary[3] != null ? ((Number) summary[3]).doubleValue() : 0.0;
            int totalProducts = summary[4] != null ? ((Number) summary[4]).intValue() : 0;
            LocalDateTime lastOrderDate = summary[5] instanceof Timestamp ? ((Timestamp) summary[5]).toLocalDateTime() : (LocalDateTime) summary[5];
            Integer lastOrderId = summary[6] != null ? (Integer) summary[6] : null;
            Integer lastPaymentId = summary[7] != null ? (Integer) summary[7] : null;

            CustomerReportDTO report = new CustomerReportDTO();
            report.setCustomerId(customerId);
            report.setCustomerName(customerName);
            report.setTotalOrders(totalOrders);
            report.setTotalSpent(totalSpent);
            report.setTotalProductsPurchased(totalProducts);
            report.setLastOrderDate(lastOrderDate);
            report.setLastOrderId(lastOrderId);
            report.setLastPaymentId(lastPaymentId);
            report.setLastPaymentStatus(PaymentStatus.COMPLETED);
            report.setSpendingCategory(determineSpendingCategory(totalSpent, totalOrders));

            reports.add(report);
        }

        return new PageImpl<>(reports, pageable, summariesPage.getTotalElements());
    }

    @Override
    public long countTotalCustomersByDateRange(LocalDate fromDate, LocalDate toDate) {
        Page<CustomerReportDTO> reportsPage = getCustomerReportsByDateRange(fromDate, toDate, Pageable.unpaged());
        return reportsPage.getTotalElements();
    }

    @Override
    public void exportCustomerReportsToExcel(HttpServletResponse response, LocalDate fromDate, LocalDate toDate) throws IOException {
        List<CustomerReportDTO> reports = getCustomerReportsByDateRange(fromDate, toDate, Pageable.unpaged()).getContent();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Customer Reports");
            String[] headers = {"STT", "Mã KH", "Tên khách hàng", "Tổng đơn hàng", "Tổng chi tiêu", "Tổng sản phẩm",
                    "Loại khách hàng", "Trạng thái", "Mã đơn gần nhất", "Mã thanh toán gần nhất",
                    "Ngày đơn hàng gần nhất"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            if (reports.isEmpty()) {
                Row emptyRow = sheet.createRow(1);
                emptyRow.createCell(0).setCellValue("Không có dữ liệu trong khoảng thời gian từ " +
                        fromDate.toString() + " đến " + toDate.toString());
            } else {
                int rowNum = 1;
                for (CustomerReportDTO report : reports) {
                    Row row = sheet.createRow(rowNum);
                    row.createCell(0).setCellValue(rowNum);
                    row.createCell(1).setCellValue(report.getCustomerId() != null ? report.getCustomerId().toString() : "N/A");
                    row.createCell(2).setCellValue(report.getCustomerName() != null ? report.getCustomerName() : "N/A");
                    row.createCell(3).setCellValue(String.valueOf(report.getTotalOrders()));
                    row.createCell(4).setCellValue(DECIMAL_FORMAT.format(report.getTotalSpent()) + " VND");
                    row.createCell(5).setCellValue(String.valueOf(report.getTotalProductsPurchased()));
                    row.createCell(6).setCellValue(report.getSpendingCategory() != null ? report.getSpendingCategory().toString() : "N/A");
                    row.createCell(7).setCellValue(report.getLastPaymentStatus() != null ? report.getLastPaymentStatus().toString() : "N/A");
                    row.createCell(8).setCellValue(report.getLastOrderId() != null ? report.getLastOrderId().toString() : "N/A");
                    row.createCell(9).setCellValue(report.getLastPaymentId() != null ? report.getLastPaymentId().toString() : "N/A");
                    row.createCell(10).setCellValue(report.getLastOrderDate() != null ? report.getLastOrderDate().format(DATE_TIME_FORMATTER) : "N/A");
                    rowNum++;
                }
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=customer_reports_" +
                    fromDate.toString() + "_to_" + toDate.toString() + ".xlsx");
            workbook.write(response.getOutputStream());
        }
    }

    private SpendingCategory determineSpendingCategory(double totalSpent, int totalOrders) {
        if (totalSpent >= 50_000_000 && totalOrders >= 10) return SpendingCategory.VIP_PREMIUM;
        if (totalSpent >= 20_000_000 && totalOrders >= 5) return SpendingCategory.POTENTIAL;
        return SpendingCategory.REGULAR;
    }
}