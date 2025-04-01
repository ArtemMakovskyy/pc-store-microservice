package com.pc.payment.kafka;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ObjectKafkaProducer {
    private final KafkaTemplate<String, SendingSimpleMailObject> kafkaTemplate;
    @Value("${kafka.topic.name}")
    private String kafkaTopicName;

    public void sendSendingObject(SendingSimpleMailObject sendingObject) {
        Message<SendingSimpleMailObject> message = MessageBuilder
                .withPayload(sendingObject)
                .setHeader(KafkaHeaders.TOPIC, kafkaTopicName)
                .build();

        kafkaTemplate.send(message);
    }
}
