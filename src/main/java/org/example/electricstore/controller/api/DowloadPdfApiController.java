package org.example.electricstore.controller.api;


import org.example.electricstore.DTO.order.OrderDTO;
import org.example.electricstore.service.common.PDFService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/download")
public class DowloadPdfApiController {
    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadInvoicePdf(@RequestBody OrderDTO orderDTO) {
        System.out.println("orderDTTO: " + orderDTO);
        PDFService pdfService = new PDFService();
        byte[] pdf = pdfService.createInvoicePDF(orderDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=invoice_" + orderDTO.getCustomerId() + ".pdf");
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

}
