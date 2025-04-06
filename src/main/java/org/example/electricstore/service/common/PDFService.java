package org.example.electricstore.service.common;

import org.example.electricstore.DTO.order.OrderDTO;
import org.example.electricstore.DTO.order.ProductOrderDTO;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Service
public class PDFService {


    public byte[] createInvoicePDF(OrderDTO orderDTO) {

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            //Font family
            InputStream fontStream = new ClassPathResource("fonts/vuArial.ttf").getInputStream();
            PdfFont vietnameseFont = PdfFontFactory.createFont(fontStream.readAllBytes(), PdfEncodings.IDENTITY_H);

            // header
            document.add(new Paragraph("HÓA ĐƠN BÁN HÀNG")
                    .setFont(vietnameseFont)
                    .setFontSize(16)
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("\n"));

            // customer info
            document.add(new Paragraph("Thông tin khách hàng:")
                    .setFont(vietnameseFont)

                    .setFontSize(12));
            document.add(new Paragraph("Tên: " + orderDTO.getCustomerDTO().getCustomerName()))
                    .setFont(vietnameseFont)
                    .setFontSize(12);
            document.add(new Paragraph("SĐT: " + orderDTO.getCustomerDTO().getPhoneNumber()))
                    .setFont(vietnameseFont)
                    .setFontSize(12);
            document.add(new Paragraph("Địa chỉ: " + orderDTO.getCustomerDTO().getAddress()))
                    .setFont(vietnameseFont)
                    .setFontSize(12);
            document.add(new Paragraph("Email: " + orderDTO.getCustomerDTO().getEmail()))
                    .setFont(vietnameseFont)
                    .setFontSize(12);
            document.add(new Paragraph("\n"));

            // product list
            Table table = new Table(UnitValue.createPercentArray(new float[]{10, 40, 15, 15, 20}));
            table.setWidth(UnitValue.createPercentValue(100));

            // cell
            table.addHeaderCell(new Cell().add(new Paragraph("STT")))
                    .setFont(vietnameseFont);

            table.addHeaderCell(new Cell().add(new Paragraph("Sản phẩm"))).setFont(vietnameseFont);
            table.addHeaderCell(new Cell().add(new Paragraph("Số lượng"))).setFont(vietnameseFont);
            table.addHeaderCell(new Cell().add(new Paragraph("Đơn giá"))).setFont(vietnameseFont);
            table.addHeaderCell(new Cell().add(new Paragraph("Thành tiền"))).setFont(vietnameseFont);

            // data product
            int index = 1;
            double total = 0;
            for (ProductOrderDTO product : orderDTO.getProductOrderDTOList()) {
                double subtotal = product.getQuantity() * product.getPriceIndex();
                total += subtotal;

                table.addCell(String.valueOf(index++)).setFont(vietnameseFont);
                table.addCell(product.getProductName()).setFont(vietnameseFont);
                table.addCell(String.valueOf(product.getQuantity())).setFont(vietnameseFont);
                table.addCell(String.format("%,d VND", product.getPriceIndex())).setFont(vietnameseFont);
                table.addCell(String.format("%,d VND", (int) subtotal)).setFont(vietnameseFont);
            }

            document.add(table);
            document.add(new Paragraph("\n"));

            // total
            document.add(new Paragraph("Tổng tiền: " + String.format("%,d VND", (int) total))
                    .setFont(vietnameseFont)
                    .setFontSize(12));


            // note
            document.add(new Paragraph("\nCảm ơn quý khách đã mua hàng!")
                    .setFont(vietnameseFont)
                    .setTextAlignment(TextAlignment.CENTER));

            document.close();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo file PDF", e);
        }
    }
}
