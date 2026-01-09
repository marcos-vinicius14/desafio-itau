package marcosvinicius.desafioitau.features.transactions.infraestructure.persistence;

import marcosvinicius.desafioitau.domain.transaction.Transaction;
import marcosvinicius.desafioitau.domain.transaction.TransactionRepository;
import marcosvinicius.desafioitau.domain.transaction.Transactions;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryTransactionRepository implements TransactionRepository {
    private final Transactions transactions = new Transactions();
    @Override
    public void save(Transaction transaction) {
        transactions.add(transaction);
    }
}
