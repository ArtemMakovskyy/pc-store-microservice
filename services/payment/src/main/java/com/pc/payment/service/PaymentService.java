package com.pc.payment.service;

import com.pc.payment.dto.PaymentRequest;
import com.pc.payment.dto.mapper.PaymentMapper;
import com.pc.payment.kafka.CreateSimpleMailObject;
import com.pc.payment.kafka.ServiceSendingObject;
import com.pc.payment.model.Payment;
import com.pc.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final ServiceSendingObject serviceSendingObject;

    public Long createPayment(PaymentRequest request) {

        final Payment payment = mapper.toPayment(request);
        final Payment savedPayment = repository.save(payment);

        String[] to = new String[]{request.customer().email()};
        CreateSimpleMailObject createSimpleMailObject
                = new CreateSimpleMailObject(
                to,
                "Info about payment",
                request.customer().firstname()  +", your order: "
                        + savedPayment.getOrderId().toString() + ". Paid by: "
                        + savedPayment.getPaymentMethod().toString()
        );
        System.out.println(createSimpleMailObject);
        serviceSendingObject.createAndSendSendingObject(createSimpleMailObject);

        return payment.getId();
    }

}
