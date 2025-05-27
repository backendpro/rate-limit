package br.com.backendpro.ratelimiter.application.usecase;

import br.com.backendpro.ratelimiter.application.dto.IdentifierDTO;
import br.com.backendpro.ratelimiter.infrastructure.service.RateLimitExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FileProcessorUseCase {
    private final Logger log = LoggerFactory.getLogger(FileProcessorUseCase.class);

    private final RateLimitExecutorService rateLimitExecutorService;

    public FileProcessorUseCase(RateLimitExecutorService rateLimitExecutorService) {
        this.rateLimitExecutorService = rateLimitExecutorService;
    }

    public void execute(IdentifierDTO request) {
        rateLimitExecutorService
                .executeParallelWithRateLimiter(request.getIdentifiers(),
                        identifiers -> {
                            log.info("Processing identifier {}", identifiers);
                        });
    }
}
