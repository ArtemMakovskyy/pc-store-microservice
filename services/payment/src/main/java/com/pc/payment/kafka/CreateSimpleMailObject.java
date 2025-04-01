package com.pc.payment.kafka;

public record CreateSimpleMailObject(
        String[] to,
        String subject,
        String body) {
}
