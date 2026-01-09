package marcosvinicius.desafioitau.domain.transaction;

import marcosvinicius.desafioitau.domain.Statistics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.DoubleSummaryStatistics;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * First Class Collection que gerencia o armazenamento em memória.
 * Implementa a lógica de estatísticas de forma performática.
 */
public class Transactions {

    private final Set<Transaction> storage = Collections.synchronizedSet(new LinkedHashSet<>());


    public void add(Transaction transaction) {
        if (transaction != null) {
            storage.add(transaction);
        }
    }


    public void clear() {
        storage.clear();
    }


    public Statistics calculateLast60Seconds() {
        DoubleSummaryStatistics stats = storage.stream()
                .filter(t -> t.happenedInTheLast(60L))
                .mapToDouble(t -> t.getAmount().doubleValue())
                .summaryStatistics();

        if (stats.getCount() == 0) {
            return new Statistics(0L, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        }

        return new Statistics(
                stats.getCount(),
                toBigDecimal(stats.getSum()),
                toBigDecimal(stats.getAverage()),
                toBigDecimal(stats.getMin()),
                toBigDecimal(stats.getMax())
        );
    }


    private BigDecimal toBigDecimal(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }


    public Set<Transaction> getAll() {
        return Collections.unmodifiableSet(storage);
    }
}