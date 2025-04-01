package com.pc.notificator.kafka;

public record SendingSimpleMailObject(
        String id,
        String[] to,
        String subject,
        String body) {
}
