package com.pc.payment.service;

import com.pc.payment.dto.PaymentNotificationRequest;
import com.pc.payment.dto.PaymentRequest;
import com.pc.payment.dto.mapper.PaymentMapper;
import com.pc.payment.model.Payment;
import com.pc.payment.repository.PaymentRepository;
import com.pc.payment.service.kafka.NotificationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;
    //  private final PaymentValidator<PaymentRequest> validator;
    private final PaymentMapper mapper;
    private final NotificationProducer notificationProducer;

    public Long createPayment(PaymentRequest request) {
//    this.validator.validate(request);
        final Payment payment = mapper.toPayment(request);
        var payment2 = repository.save(payment);
//        notification(request);
        return payment.getId();
    }

    private void notification(PaymentRequest request) {
        this.notificationProducer.sendNotification(new PaymentNotificationRequest(
                request.orderReference(),
                request.amount(),
                request.paymentMethod(),
                request.customer().firstname(),
                request.customer().lastname(),
                request.customer().email()
        ));
    }
}
