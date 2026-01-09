package marcosvinicius.desafioitau.domain.transaction;

import marcosvinicius.desafioitau.domain.Statistics;

public interface TransactionRepository {
    void save(Transaction transaction);
    void clearAll();
    Statistics getStatistics();
}
