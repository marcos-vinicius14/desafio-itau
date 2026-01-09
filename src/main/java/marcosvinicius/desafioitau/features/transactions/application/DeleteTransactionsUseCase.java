package marcosvinicius.desafioitau.features.transactions.application;

import marcosvinicius.desafioitau.domain.transaction.TransactionRepository;

public final class DeleteTransactionsUseCase {
    private final TransactionRepository repository;

    public DeleteTransactionsUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public void execute() {
        repository.clearAll();
    }
}
