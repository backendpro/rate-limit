package br.com.backendpro.ratelimiter.infrastructure.service;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RateLimitExecutorServiceTest {

    private RateLimiter rateLimiter;
    private RateLimitExecutorService executorService;

    @BeforeEach
    void setUp() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(2)
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .timeoutDuration(Duration.ofMillis(100))
                .build();

        rateLimiter = mock(RateLimiter.class);
        when(rateLimiter.getRateLimiterConfig()).thenReturn(config);

        executorService = new RateLimitExecutorService(rateLimiter);
    }

    @Test
    void shouldExecuteTaskWhenPermitted() throws InterruptedException {
        // Arrange
        when(rateLimiter.acquirePermission()).thenReturn(true);
        AtomicInteger counter = new AtomicInteger(0);

        // Act
        executorService.executeParallelWithRateLimiter(List.of("item1", "item2"), item -> counter.incrementAndGet());

        // Espera as tarefas finalizarem
        Thread.sleep(300);

        // Assert
        assertEquals(2, counter.get());
    }

    @Test
    void shouldNotExecuteTaskWhenRateLimiterDenies() throws InterruptedException {
        // Arrange
        when(rateLimiter.acquirePermission()).thenReturn(false);
        Consumer<String> consumer = mock(Consumer.class);

        // Act
        executorService.executeParallelWithRateLimiter(List.of("item1", "item2"), consumer);

        // Espera as tarefas finalizarem
        Thread.sleep(300);

        // Assert
        verify(consumer, never()).accept(any());
    }

    @Test
    void shouldNotExecuteTaskWhenSemaphoreBlocked() throws InterruptedException {
        when(rateLimiter.acquirePermission()).thenReturn(true);

        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(1)
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .timeoutDuration(Duration.ofMillis(50)) // pouco tempo de espera
                .build();

        RateLimiter limitedLimiter = mock(RateLimiter.class);
        when(limitedLimiter.getRateLimiterConfig()).thenReturn(config);
        when(limitedLimiter.acquirePermission()).thenReturn(true);

        RateLimitExecutorService limitedService = new RateLimitExecutorService(limitedLimiter);

        AtomicInteger counter = new AtomicInteger(0);

        Consumer<String> consumer = item -> {
            counter.incrementAndGet();
            try {
                Thread.sleep(200); // força manter o semáforo ocupado
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        limitedService.executeParallelWithRateLimiter(List.of("A", "B"), consumer);

        Thread.sleep(500); // aguarda execução

        assertEquals(1, counter.get()); // só uma task conseguiu entrar
    }
}
