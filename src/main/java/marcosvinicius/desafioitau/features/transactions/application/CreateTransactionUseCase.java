package marcosvinicius.desafioitau.features.transactions.application;

import marcosvinicius.desafioitau.domain.transaction.Transaction;
import marcosvinicius.desafioitau.domain.transaction.TransactionRepository;
import marcosvinicius.desafioitau.features.transactions.api.CreateTransactionRequest;

public final class CreateTransactionUseCase {
    private final TransactionRepository repository;

    public CreateTransactionUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public void execute(CreateTransactionRequest request) {
        Transaction transaction = new Transaction(request.valor(), request.dataHora());
        repository.save(transaction);
        System.out.println("Transação registrada com sucesso: " + transaction.getId());
    }
}
