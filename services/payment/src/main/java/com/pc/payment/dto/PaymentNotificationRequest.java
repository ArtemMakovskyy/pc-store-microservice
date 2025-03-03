package com.pc.payment.dto;

import com.pc.payment.model.PaymentMethod;
import java.math.BigDecimal;

public record PaymentNotificationRequest(
        String orderReference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        String customerFirstName,
        String customerLastName,

        String customerEmail

) {
}
