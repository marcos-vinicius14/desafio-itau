package marcosvinicius.desafioitau.features.transactions.application;

import marcosvinicius.desafioitau.domain.transaction.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteTransactionsUseCase {
    private final TransactionRepository repository;

    public DeleteTransactionsUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public void execute() {
        repository.clearAll();
    }
}
