package marcosvinicius.desafioitau.features.statistics.application;

import marcosvinicius.desafioitau.domain.Statistics;
import marcosvinicius.desafioitau.domain.transaction.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetStatisticsUseCase {
    private static final Logger log = LoggerFactory.getLogger(GetStatisticsUseCase.class);
    private final TransactionRepository repository;

    public GetStatisticsUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public Statistics execute() {
        log.info("Calculando estatísticas das transações dos últimos 60 segundos");
        Statistics stats = repository.getStatistics();
        log.info("Estatísticas calculadas: {}", stats);
        return stats;
    }
}
