package com.pc.order.dto;

import com.pc.order.model.PaymentMethod;
import java.math.BigDecimal;

public record PaymentRequest(
    BigDecimal amount,
    PaymentMethod paymentMethod,
    Long orderId,
    String orderReference,
    CustomerResponse customer
) {
}
