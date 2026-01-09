package marcosvinicius.desafioitau.features.transactions.api;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TransactionRequest(
        @NotBlank
        BigDecimal valor,

        @NotBlank
        OffsetDateTime dataHora
) {
}
