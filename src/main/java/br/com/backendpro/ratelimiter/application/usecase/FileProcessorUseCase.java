package br.com.backendpro.ratelimiter.application.usecase;

import br.com.backendpro.ratelimiter.application.dto.IdentifierDTO;
import br.com.backendpro.ratelimiter.infrastructure.service.RateLimitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;

@Service
public class FileProcessorUseCase {
    private final Logger log = LoggerFactory.getLogger(FileProcessorUseCase.class);

    private final RateLimitService rateLimitService;

    public FileProcessorUseCase(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    public void execute(IdentifierDTO request) {
        if (rateLimitService.executeWithRateLimit()) {
            request.getIdentifiers().forEach(identifier -> {
                Executors.newVirtualThreadPerTaskExecutor()
                        .execute(() -> {
                            System.out.println(identifier);
                        });
            });
        }
    }
}
