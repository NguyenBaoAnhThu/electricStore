package org.example.electricstore.service.impl;

import org.example.electricstore.enums.PaymentMethod;
import org.example.electricstore.enums.PaymentStatus;
import org.example.electricstore.model.Order;
import org.example.electricstore.model.Payment;
import org.example.electricstore.repository.IOrderRepository;
import org.example.electricstore.repository.IPaymentRepository;
import org.example.electricstore.service.interfaces.IPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService implements IPaymentService {
    @Autowired
    private IPaymentRepository paymentRepository;
    @Autowired
    private IOrderRepository orderRepository;

    @Override
    public Integer addPayment(Integer orderId, Integer paymentType) {
        Payment payment = new Payment();
        Order order = orderRepository.findByOrderID(orderId);
        PaymentStatus paymentStatus = PaymentStatus.PENDING;
        PaymentMethod paymentMethod = PaymentMethod.CASH;
        if(paymentType == 1){
            paymentStatus = PaymentStatus.PENDING;
            paymentMethod = PaymentMethod.ONLINE_BANKING;
        } else if (paymentType == 2){
            paymentStatus = PaymentStatus.COMPLETED;
            paymentMethod = PaymentMethod.CASH;
        }else {
            paymentStatus = PaymentStatus.FAILED;
            paymentMethod = PaymentMethod.CREDIT_CARD;
        }

        payment.setOrder(order);
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus(paymentStatus);
        payment.setCreateAt(java.time.LocalDateTime.now());
        return paymentRepository.save(payment).getPaymentID();
    }

    @Override
    public void updatePayment(Integer paymentId, Integer amount) {
        Payment payment = paymentRepository
                .findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setAmount(amount);
        payment.setStatus(PaymentStatus.COMPLETED);
        paymentRepository.save(payment);
    }


}
