package br.com.backendpro.ratelimiter.entrypoint.rest;

import br.com.backendpro.ratelimiter.application.dto.IdentifierDTO;
import br.com.backendpro.ratelimiter.application.usecase.FileProcessorUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/v1")
public class TriggerProcessorController {

    private final Logger log = LoggerFactory.getLogger(TriggerProcessorController.class);
    private final FileProcessorUseCase fileProcessorUseCase;

    public TriggerProcessorController(FileProcessorUseCase fileProcessorUseCase) {
        this.fileProcessorUseCase = fileProcessorUseCase;
    }

    @PostMapping
    public ResponseEntity<String> triggerProcessor(@RequestBody IdentifierDTO request) {
        log.info("Starting triggerProcessor {}", LocalDateTime.now());
        fileProcessorUseCase.execute(request);
        return ResponseEntity.ok("Triggered...");
    }
}
