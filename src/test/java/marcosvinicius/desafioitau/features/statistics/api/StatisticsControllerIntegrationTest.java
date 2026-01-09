package marcosvinicius.desafioitau.features.statistics.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("StatisticsController")
class StatisticsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc.perform(delete("/transacao"));
    }

    @Nested
    @DisplayName("GET /estatistica")
    class GetStatistics {

        @Test
        @DisplayName("deve retornar estatísticas zeradas quando não há transações")
        void shouldReturnZeroStatisticsWhenNoTransactions() throws Exception {
            mockMvc.perform(get("/estatistica")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count").value(0))
                    .andExpect(jsonPath("$.sum").value(0))
                    .andExpect(jsonPath("$.avg").value(0))
                    .andExpect(jsonPath("$.min").value(0))
                    .andExpect(jsonPath("$.max").value(0));
        }

        @Test
        @DisplayName("deve retornar estatísticas com uma transação")
        void shouldReturnStatisticsWithOneTransaction() throws Exception {
            // Cria uma transação
            String requestBody = """
                {
                    "valor": 150.00,
                    "dataHora": "%s"
                }
                """.formatted(OffsetDateTime.now().minusSeconds(30).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

            mockMvc.perform(post("/transacao")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isCreated());

            // Verifica estatísticas
            mockMvc.perform(get("/estatistica")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count").value(1))
                    .andExpect(jsonPath("$.sum").value(150.00))
                    .andExpect(jsonPath("$.avg").value(150.00))
                    .andExpect(jsonPath("$.min").value(150.00))
                    .andExpect(jsonPath("$.max").value(150.00));
        }

        @Test
        @DisplayName("deve retornar estatísticas com múltiplas transações")
        void shouldReturnStatisticsWithMultipleTransactions() throws Exception {
            // Cria três transações
            String[] values = {"100.00", "200.00", "300.00"};
            for (String value : values) {
                String requestBody = """
                    {
                        "valor": %s,
                        "dataHora": "%s"
                    }
                    """.formatted(value, OffsetDateTime.now().minusSeconds(30).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

                mockMvc.perform(post("/transacao")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                        .andExpect(status().isCreated());
            }

            // Verifica estatísticas
            mockMvc.perform(get("/estatistica")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count").value(3))
                    .andExpect(jsonPath("$.sum").value(600.00))
                    .andExpect(jsonPath("$.avg").value(200.00))
                    .andExpect(jsonPath("$.min").value(100.00))
                    .andExpect(jsonPath("$.max").value(300.00));
        }

        @Test
        @DisplayName("deve ignorar transações mais antigas que 60 segundos")
        void shouldIgnoreTransactionsOlderThan60Seconds() throws Exception {
            // Cria uma transação recente
            String recentRequest = """
                {
                    "valor": 100.00,
                    "dataHora": "%s"
                }
                """.formatted(OffsetDateTime.now().minusSeconds(30).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

            mockMvc.perform(post("/transacao")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(recentRequest))
                    .andExpect(status().isCreated());

            // Cria uma transação antiga (mais de 60 segundos)
            String oldRequest = """
                {
                    "valor": 500.00,
                    "dataHora": "%s"
                }
                """.formatted(OffsetDateTime.now().minusSeconds(90).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

            mockMvc.perform(post("/transacao")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(oldRequest))
                    .andExpect(status().isCreated());

            // Verifica que apenas a transação recente é considerada
            mockMvc.perform(get("/estatistica")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count").value(1))
                    .andExpect(jsonPath("$.sum").value(100.00));
        }

        @Test
        @DisplayName("deve retornar estatísticas zeradas após deletar transações")
        void shouldReturnZeroStatisticsAfterDeletingTransactions() throws Exception {
            // Cria uma transação
            String requestBody = """
                {
                    "valor": 150.00,
                    "dataHora": "%s"
                }
                """.formatted(OffsetDateTime.now().minusSeconds(30).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

            mockMvc.perform(post("/transacao")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isCreated());

            // Deleta todas as transações
            mockMvc.perform(delete("/transacao"))
                    .andExpect(status().isOk());

            // Verifica que as estatísticas estão zeradas
            mockMvc.perform(get("/estatistica")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count").value(0))
                    .andExpect(jsonPath("$.sum").value(0));
        }
    }
}
