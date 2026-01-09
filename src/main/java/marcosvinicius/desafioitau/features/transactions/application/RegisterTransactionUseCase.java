package marcosvinicius.desafioitau.features.transactions.application;

import marcosvinicius.desafioitau.domain.transaction.Transaction;
import marcosvinicius.desafioitau.domain.transaction.TransactionRepository;
import marcosvinicius.desafioitau.features.transactions.api.TransactionRequest;
import org.springframework.stereotype.Service;

@Service
public class RegisterTransactionUseCase {
    private final TransactionRepository repository;

    public RegisterTransactionUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public void execute(TransactionRequest request) {
        Transaction transaction = new Transaction(request.valor(), request.dataHora());

        repository.save(transaction);

        System.out.println(STR."Transação registrada com sucesso: \{transaction.getId()}");
    }
}
