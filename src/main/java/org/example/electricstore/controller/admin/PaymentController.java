package org.example.electricstore.controller.admin;

import org.example.electricstore.service.interfaces.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private IPaymentService paymentService;

    @Value("${vnpay.tmnCode}")
    private String vnp_TmnCode;

    @Value("${vnpay.hashSecret}")
    private String vnp_HashSecret;

    @Value("${vnpay.payUrl}")
    private String vnp_PayUrl;

    @Value("${vnpay.returnUrl}")
    private String vnp_ReturnUrl;

    @GetMapping("/vnpay")
    public ResponseEntity<?> createPayment(@RequestParam double amount, @RequestParam Integer paymentId) {
        try {
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String orderType = "other";
            long amountInVND = (long) (amount * 100); // VNPAY tính theo đơn vị nhỏ nhất (VD: 100000 = 1.000 VND)
            String vnp_TxnRef = String.valueOf(System.currentTimeMillis());

            // Tạo thời gian giao dịch
            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());

            // Thông tin IP khách hàng
            String ipAddr = "127.0.0.1"; // Cần thay bằng IP thực khi triển khai

            // Tạo danh sách tham số
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amountInVND));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang: " + vnp_TxnRef);
            vnp_Params.put("vnp_OrderType", orderType);
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl + "?paymentId=" + paymentId);
            vnp_Params.put("vnp_IpAddr", ipAddr);
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);


            // Sắp xếp tham số theo thứ tự bảng chữ cái
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);

            StringBuilder query = new StringBuilder();
            StringBuilder hashData = new StringBuilder();

            for (String fieldName : fieldNames) {
                String fieldValue = vnp_Params.get(fieldName);
                if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                    hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8)).append('&');
                    query.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8)).append('&');
                }
            }

            // Loại bỏ ký tự `&` cuối cùng
            hashData.deleteCharAt(hashData.length() - 1);
            query.deleteCharAt(query.length() - 1);

            // Tạo chữ ký bảo mật (SecureHash)
            String vnp_SecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
            query.append("&vnp_SecureHash=").append(vnp_SecureHash);

            // Tạo URL thanh toán
            String paymentUrl = vnp_PayUrl + "?" + query.toString();

            return ResponseEntity.ok(Map.of("paymentUrl", paymentUrl));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating payment");
        }
    }

    // Hàm tạo SecureHash (SHA512)
    private String hmacSHA512(String key, String data) throws Exception {
        try {

            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<?> vnpayReturn(@RequestParam Map<String, String> requestParams) {
        String vnp_ResponseCode = requestParams.get("vnp_ResponseCode");
        String vnp_SecureHash = requestParams.get("vnp_SecureHash");

        // Xác thực chữ ký
        StringBuilder hashData = new StringBuilder();
        List<String> fieldNames = new ArrayList<>(requestParams.keySet());
        Collections.sort(fieldNames);

        for (String fieldName : fieldNames) {
            if (!fieldName.equals("vnp_SecureHash") && fieldName.startsWith("vnp_")) {
                hashData.append(fieldName).append('=').append(requestParams.get(fieldName)).append('|');
            }
        }

        if (hashData.length() > 0) {
            hashData.deleteCharAt(hashData.length() - 1);
        }



        try {
            String calculatedHash = hmacSHA512(vnp_HashSecret, hashData.toString());

            // So sánh chữ ký
//            if (!calculatedHash.equalsIgnoreCase(vnp_SecureHash)) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
//            }

            if ("00".equals(vnp_ResponseCode)) {
                Integer paymentId = Integer.parseInt(requestParams.get("paymentId"));
                Integer amount = Integer.parseInt(requestParams.get("vnp_Amount")) / 100;
                paymentService.updatePayment(paymentId, amount);
                return ResponseEntity.ok("Thanh toán thành công");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Thanh toán thất bại");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing payment response");
        }
    }
}
