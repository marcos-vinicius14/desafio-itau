package marcosvinicius.desafioitau.features.statistics.application;

import marcosvinicius.desafioitau.domain.Statistics;
import marcosvinicius.desafioitau.domain.transaction.TransactionRepository;

public class GetStatisticsUseCase {
    private final TransactionRepository repository;

    public GetStatisticsUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public Statistics execute() {
       return repository.getStatistics();
    }
}
