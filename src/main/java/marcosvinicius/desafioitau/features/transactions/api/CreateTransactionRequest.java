package marcosvinicius.desafioitau.features.transactions.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CreateTransactionRequest(
        @NotNull(message = "O valor da transação é obrigatório!")
        BigDecimal valor,

        @NotNull(message = "A data para a transação é obrigatória!")
        OffsetDateTime dataHora
) {
}
