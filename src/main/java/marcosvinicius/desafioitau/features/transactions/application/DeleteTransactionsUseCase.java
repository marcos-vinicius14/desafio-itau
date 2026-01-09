package marcosvinicius.desafioitau.features.transactions.application;

import marcosvinicius.desafioitau.domain.transaction.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public final class DeleteTransactionsUseCase {
    private static final Logger log = LoggerFactory.getLogger(DeleteTransactionsUseCase.class);
    private final TransactionRepository repository;

    public DeleteTransactionsUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public void execute() {
        log.info("Iniciando remoção de todas as transações");
        repository.clearAll();
        log.info("Todas as transações foram removidas com sucesso");
    }
}
