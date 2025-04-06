package org.example.electricstore.config;

import com.itextpdf.kernel.pdf.PdfWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayOutputStream;

@Configuration
public class PDFConfig {

    @Bean
    public PdfWriter pdfWriter() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        return new PdfWriter(byteArrayOutputStream);
    }
}
