package com.pc.payment.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class KafkaPaymentConfiguration {

    @Value("${kafka.topic.name}")
    private String topicName;

    @Bean
    public NewTopic newProductTopic() {
        return new NewTopic(topicName, 1, (short) 1);
    }
}
