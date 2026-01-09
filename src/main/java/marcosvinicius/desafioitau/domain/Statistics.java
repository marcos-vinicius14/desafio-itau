package marcosvinicius.desafioitau.domain;

import java.math.BigDecimal;

public record Statistics(
        Long count,
        BigDecimal sum,
        BigDecimal avg,
        BigDecimal min,
        BigDecimal max
) {
}
