package br.com.backendpro.ratelimiter.infrastructure.config;

import io.github.resilience4j.ratelimiter.RateLimiter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RateLimiterBeanTest {
    @Autowired
    private RateLimiter rateLimiter;

    @Test
    void shouldLoadRateLimiterBean() {
        assertNotNull(rateLimiter);
    }
}
