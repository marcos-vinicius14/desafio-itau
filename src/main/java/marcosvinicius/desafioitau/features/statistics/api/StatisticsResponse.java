package marcosvinicius.desafioitau.features.statistics.api;

import marcosvinicius.desafioitau.domain.Statistics;

import java.math.BigDecimal;

public record StatisticsResponse(
        Long count,
        BigDecimal sum,
        BigDecimal avg,
        BigDecimal min,
        BigDecimal max
) {
    public static StatisticsResponse from(Statistics stats) {
        return new StatisticsResponse(
                stats.count(), stats.sum(), stats.avg(), stats.min(), stats.max()
        );
    }
}