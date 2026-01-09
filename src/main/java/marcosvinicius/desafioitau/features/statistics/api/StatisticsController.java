package marcosvinicius.desafioitau.features.statistics.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import marcosvinicius.desafioitau.features.statistics.application.GetStatisticsUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estatistica")
@Tag(name = "Estatisticas", description = "Endpoint para exibir estatisticas de todas as transações")
public class StatisticsController {
    private final GetStatisticsUseCase getStatisticsUseCase;

    public StatisticsController(GetStatisticsUseCase getStatisticsUseCase) {
        this.getStatisticsUseCase = getStatisticsUseCase;
    }

    @Operation(summary = "Retorna as estatisticas de todas as transações",
            description = "Retorna a soma de todas as transações, o total em transaões, a media, min e max"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estatisticas retornadas")
    })
    @GetMapping
    public ResponseEntity<StatisticsResponse> getStatistics() {
        return ResponseEntity.ok(StatisticsResponse.from(getStatisticsUseCase.execute()));
    }
}
