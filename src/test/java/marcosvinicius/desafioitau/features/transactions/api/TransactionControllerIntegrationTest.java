package marcosvinicius.desafioitau.features.transactions.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TransactionController")
class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc.perform(delete("/transacao"));
    }

    @Nested
    @DisplayName("POST /transacao")
    class CreateTransaction {

        @Test
        @DisplayName("deve criar transação e retornar 201")
        void shouldCreateTransactionAndReturn201() throws Exception {
            String requestBody = """
                {
                    "valor": 100.50,
                    "dataHora": "%s"
                }
                """.formatted(OffsetDateTime.now().minusSeconds(30).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

            mockMvc.perform(post("/transacao")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("deve retornar 422 quando valor é negativo")
        void shouldReturn422WhenAmountIsNegative() throws Exception {
            String requestBody = """
                {
                    "valor": -50.00,
                    "dataHora": "%s"
                }
                """.formatted(OffsetDateTime.now().minusSeconds(30).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

            mockMvc.perform(post("/transacao")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isUnprocessableEntity());
        }

        @Test
        @DisplayName("deve retornar 422 quando data está no futuro")
        void shouldReturn422WhenDateIsInFuture() throws Exception {
            String requestBody = """
                {
                    "valor": 100.00,
                    "dataHora": "%s"
                }
                """.formatted(OffsetDateTime.now().plusMinutes(5).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

            mockMvc.perform(post("/transacao")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isUnprocessableEntity());
        }

        @Test
        @DisplayName("deve retornar 400 quando valor é nulo")
        void shouldReturn400WhenAmountIsNull() throws Exception {
            String requestBody = """
                {
                    "dataHora": "%s"
                }
                """.formatted(OffsetDateTime.now().minusSeconds(30).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

            mockMvc.perform(post("/transacao")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("deve retornar 400 quando data é nula")
        void shouldReturn400WhenDateIsNull() throws Exception {
            String requestBody = """
                {
                    "valor": 100.00
                }
                """;

            mockMvc.perform(post("/transacao")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("deve retornar 400 quando JSON é inválido")
        void shouldReturn400WhenJsonIsInvalid() throws Exception {
            String invalidJson = "{ invalid json }";

            mockMvc.perform(post("/transacao")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("DELETE /transacao")
    class DeleteTransactions {

        @Test
        @DisplayName("deve deletar todas as transações e retornar 200")
        void shouldDeleteAllTransactionsAndReturn200() throws Exception {
            // Cria uma transação primeiro
            String requestBody = """
                {
                    "valor": 100.00,
                    "dataHora": "%s"
                }
                """.formatted(OffsetDateTime.now().minusSeconds(30).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));

            mockMvc.perform(post("/transacao")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isCreated());

            // Deleta todas
            mockMvc.perform(delete("/transacao"))
                    .andExpect(status().isOk());
        }
    }
}
