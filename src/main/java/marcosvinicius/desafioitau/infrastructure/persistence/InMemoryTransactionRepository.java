package marcosvinicius.desafioitau.infrastructure.persistence;

import marcosvinicius.desafioitau.domain.Statistics;
import marcosvinicius.desafioitau.domain.transaction.Transaction;
import marcosvinicius.desafioitau.domain.transaction.TransactionRepository;
import marcosvinicius.desafioitau.domain.transaction.Transactions;
import org.springframework.stereotype.Repository;

@Repository
public final class InMemoryTransactionRepository implements TransactionRepository {
    private final Transactions transactions = new Transactions();

    @Override
    public void save(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public void clearAll() {
        transactions.clear();
    }

    @Override
    public Statistics getStatistics() {
        return transactions.calculateLast60Seconds();
    }
}
