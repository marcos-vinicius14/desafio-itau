package marcosvinicius.desafioitau.features.transactions.config;

import marcosvinicius.desafioitau.domain.transaction.TransactionRepository;
import marcosvinicius.desafioitau.features.transactions.application.CreateTransactionUseCase;
import marcosvinicius.desafioitau.features.transactions.application.DeleteTransactionsUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionConfig {
    @Bean
    public CreateTransactionUseCase createTransactionUseCase(TransactionRepository repository) {
        return new CreateTransactionUseCase(repository);
    }

    @Bean
    public DeleteTransactionsUseCase deleteTransactionsUseCase(TransactionRepository repository) {
        return new DeleteTransactionsUseCase(repository);
    }
}
