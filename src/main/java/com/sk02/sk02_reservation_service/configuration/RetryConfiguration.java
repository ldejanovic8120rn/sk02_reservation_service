package com.sk02.sk02_reservation_service.configuration;

import com.sk02.sk02_reservation_service.exception.NotFoundException;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RetryConfiguration {

    @Bean
    public Retry userServiceRetry(){

        RetryConfig retryConfig = RetryConfig.custom()
                .intervalFunction(IntervalFunction.ofExponentialBackoff(2000, 2))
                .maxAttempts(5)
                .ignoreExceptions(NotFoundException.class)
                .build();

        RetryRegistry registry = RetryRegistry.of(retryConfig);

        return registry.retry("userServiceRetry");
    }
}
