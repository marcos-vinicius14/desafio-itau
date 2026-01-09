package marcosvinicius.desafioitau.features.statistics.api;

import marcosvinicius.desafioitau.features.statistics.application.GetStatisticsUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estatistica")
public class StatisticsController {
    private final GetStatisticsUseCase getStatisticsUseCase;

    public StatisticsController(GetStatisticsUseCase getStatisticsUseCase) {
        this.getStatisticsUseCase = getStatisticsUseCase;
    }

    @GetMapping
    public ResponseEntity<StatisticsResponse> getStatistics() {
        return ResponseEntity.ok(StatisticsResponse.from(getStatisticsUseCase.execute()));
    }
}
