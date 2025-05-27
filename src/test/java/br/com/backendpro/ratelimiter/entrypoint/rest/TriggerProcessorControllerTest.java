package br.com.backendpro.ratelimiter.entrypoint.rest;

import br.com.backendpro.ratelimiter.application.dto.IdentifierDTO;
import br.com.backendpro.ratelimiter.application.usecase.FileProcessorUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TriggerProcessorController.class)
public class TriggerProcessorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FileProcessorUseCase fileProcessorUseCase;

    @Test
    void shouldTriggerProcessorAndReturnOk() throws Exception {
        // Arrange
        String json = """
                {
                    "identifiers": ["id1", "id2", "id3"]
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Triggered..."));

        // Verify interaction
        Mockito.verify(fileProcessorUseCase).execute(
                new IdentifierDTO(List.of("id1", "id2", "id3"))
        );
    }
}
