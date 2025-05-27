package br.com.backendpro.ratelimiter.application.usecase;

import br.com.backendpro.ratelimiter.application.dto.IdentifierDTO;
import br.com.backendpro.ratelimiter.infrastructure.service.RateLimitExecutorService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.function.Consumer;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class FileProcessorUseCaseTest {

    @Test
    void shouldDelegateProcessingToRateLimitExecutorService() {
        // Arrange
        RateLimitExecutorService executorService = mock(RateLimitExecutorService.class);
        FileProcessorUseCase useCase = new FileProcessorUseCase(executorService);
        IdentifierDTO dto = new IdentifierDTO(of("id1", "id2"));

        // Act
        useCase.execute(dto);

        // Assert
        ArgumentCaptor<List<String>> listCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<Consumer<String>> consumerCaptor = ArgumentCaptor.forClass(Consumer.class);

        verify(executorService, times(1))
                .executeParallelWithRateLimiter(listCaptor.capture(), consumerCaptor.capture());

        assertEquals(of("id1", "id2"), listCaptor.getValue());
    }
}
