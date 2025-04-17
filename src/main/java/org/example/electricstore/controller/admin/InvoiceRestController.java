package org.example.electricstore.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.electricstore.model.Invoice;
import org.example.electricstore.model.InvoiceItem;
import org.example.electricstore.repository.InvoiceRepository;
import org.example.electricstore.repository.InvoiceItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/Admin/invoice")
@RequiredArgsConstructor
@CrossOrigin("*")
public class InvoiceRestController {

    private final InvoiceRepository invoiceRepo;
    private final InvoiceItemRepository invoiceItemRepo;

    @PostMapping
    public ResponseEntity<String> saveInvoice(@RequestBody Invoice invoice) {
        try {
            invoiceRepo.save(invoice); // Lưu invoice trước để có ID

            List<InvoiceItem> items = invoice.getProducts();
            if (items != null) {
                for (InvoiceItem item : items) {
                    item.setInvoice(invoice); // Gắn lại hóa đơn sau khi có ID
                }
                invoiceItemRepo.saveAll(items);
            }
            return ResponseEntity.ok("Lưu hóa đơn thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống khi lưu hóa đơn: " + e.getMessage());
        }
    }


    @PostMapping("/cancel")
    @ResponseBody
    public ResponseEntity<String> cancelInvoice(@RequestParam Long invoiceId) {
        Invoice invoice = invoiceRepo.findByIdWithProducts(invoiceId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu nhập"));

        for (InvoiceItem item : invoice.getProducts()) {
            item.setPaymentStatus("ĐÃ HỦY");
        }

        invoiceRepo.save(invoice);

        return ResponseEntity.ok("Đã hủy phiếu nhập thành công.");
    }


}
