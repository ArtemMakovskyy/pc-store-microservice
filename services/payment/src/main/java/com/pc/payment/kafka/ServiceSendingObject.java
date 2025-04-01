package com.pc.payment.kafka;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceSendingObject {
    private final ObjectKafkaProducer objectKafkaProducer;

    public SendingSimpleMailObject createAndSendSendingObject(CreateSimpleMailObject simpleMailObject) {
        final SendingSimpleMailObject sendingObject = new SendingSimpleMailObject(
                UUID.randomUUID().toString(),
                simpleMailObject.to(),
                simpleMailObject.subject(),
                simpleMailObject.body());
        objectKafkaProducer.sendSendingObject(sendingObject);
        return sendingObject;
    }
}
