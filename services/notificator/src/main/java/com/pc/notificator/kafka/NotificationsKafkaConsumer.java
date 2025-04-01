package com.pc.notificator.kafka;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationsKafkaConsumer {

    @KafkaListener(topics = "simple-mail-topic")
    public void consume(SendingSimpleMailObject sendingObject) throws Exception {
        log.info(sendingObject.id());
        System.out.println(sendingObject.subject());
        System.out.println(sendingObject.body());
    }
}