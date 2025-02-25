package com.pc.notificator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final int QUEUE_CAPACITY = 500;
    private static final String THREAD_NAME_PREFIX = "AsyncThread-";

    /**
     * Email Sending Strategy:
     * < 1000 emails per hour  ->  1–2 threads  ->  @Async
     * 1000 – 10 000 per hour  ->  5–10 threads ->  ThreadPoolTaskExecutor
     * 100 000+ per day        ->  10–50 threads ->  Kafka/RabbitMQ + Spring Batch
     */

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        //Spring Boot without Kafka optimal sets
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        executor.initialize();
        return executor;
    }
}
