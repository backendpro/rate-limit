package br.com.backendpro.ratelimiter.infrastructure.service;

import io.github.resilience4j.ratelimiter.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Service
public class RateLimitExecutorService {

    private static final Logger log = LoggerFactory.getLogger(RateLimitExecutorService.class);
    private final RateLimiter rateLimiter;
    private final ExecutorService executorService;
    private final Semaphore semaphore;
    private final Long timeoutMillis;

    public RateLimitExecutorService(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
        this.executorService = Executors.newVirtualThreadPerTaskExecutor();
        this.semaphore = new Semaphore(rateLimiter.getRateLimiterConfig().getLimitForPeriod());
        this.timeoutMillis = rateLimiter.getRateLimiterConfig().getTimeoutDuration().toMillis();
    }

    public <T> void executeParallelWithRateLimiter(List<T> items, Consumer<T> task) {
        for (T item : items) {
            executorService.execute(() -> {
                boolean acquired = false;
                try {
                    acquired = semaphore.tryAcquire(timeoutMillis, MILLISECONDS);
                    if (acquired) {
                        if (rateLimiter.acquirePermission()) {
                            task.accept(item);
                        } else {
                            log.warn("Rate limit exceeded for item {}", item);
                        }
                    } else {
                        log.warn("Thread limit reached for item {}", item);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    if (acquired) {
                        semaphore.release();
                    }
                }
            });
        }
    }
}
