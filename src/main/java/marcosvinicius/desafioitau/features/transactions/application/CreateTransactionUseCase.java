package marcosvinicius.desafioitau.features.transactions.application;

import marcosvinicius.desafioitau.domain.transaction.Transaction;
import marcosvinicius.desafioitau.domain.transaction.TransactionRepository;
import marcosvinicius.desafioitau.features.transactions.api.CreateTransactionRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public final class CreateTransactionUseCase {
    private static final Logger log = LoggerFactory.getLogger(CreateTransactionUseCase.class);
    private final TransactionRepository repository;

    public CreateTransactionUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public void execute(CreateTransactionRequest request) {
        Transaction transaction = new Transaction(request.valor(), request.dataHora());
        repository.save(transaction);
        log.info("Transação registrada com sucesso: {}", transaction.getId());
    }
}
