package br.com.backendpro.ratelimiter.infrastructure.config;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {

    @Bean
    public RateLimiter rateLimiter() {

        RateLimiterRegistry registry =
                RateLimiterRegistry.of(RateLimiterConfig
                        .custom()
                        .limitForPeriod(5)
                        .limitRefreshPeriod(Duration.ofSeconds(1))
                        .timeoutDuration(Duration.ofMillis(500))
                        .build());

        return registry.rateLimiter("rateLimiter");
    }
}
