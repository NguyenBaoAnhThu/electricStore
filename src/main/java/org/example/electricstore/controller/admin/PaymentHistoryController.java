package org.example.electricstore.controller.admin;

import org.example.electricstore.model.PaymentHistory;
import org.example.electricstore.repository.PaymentHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-history")
public class PaymentHistoryController {

    @Autowired
    private PaymentHistoryRepository historyRepo;

    @PostMapping
    public void save(@RequestBody PaymentHistory history) {
        historyRepo.save(history);
    }

    @GetMapping
    public List<PaymentHistory> getByInvoiceId(@RequestParam Long invoiceId) {
        return historyRepo.findByInvoiceId(invoiceId);
    }
}