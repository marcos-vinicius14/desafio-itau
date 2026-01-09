package marcosvinicius.desafioitau.features.transactions.api;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CreateTransactionRequest(
        @NotBlank(message = "O valor da transação é obrigatório!")
        BigDecimal valor,

        @NotBlank(message = "A data para a transação é obrigatória!")
        OffsetDateTime dataHora
) {
}
