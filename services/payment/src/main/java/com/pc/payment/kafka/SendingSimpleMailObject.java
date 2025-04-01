package com.pc.payment.kafka;

public record SendingSimpleMailObject(
        String id,
        String[] to,
        String subject,
        String body) {
}
