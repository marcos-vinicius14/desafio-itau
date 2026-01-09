package marcosvinicius.desafioitau.features.statistics.config;

import marcosvinicius.desafioitau.domain.transaction.TransactionRepository;
import marcosvinicius.desafioitau.features.statistics.application.GetStatisticsUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatisticsConfig {
    @Bean
    public GetStatisticsUseCase getStatisticsUseCase(TransactionRepository repository) {
        return new GetStatisticsUseCase(repository);
    }
}
