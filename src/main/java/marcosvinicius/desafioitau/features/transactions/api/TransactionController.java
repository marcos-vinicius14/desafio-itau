package marcosvinicius.desafioitau.features.transactions.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import marcosvinicius.desafioitau.features.transactions.application.CreateTransactionUseCase;
import marcosvinicius.desafioitau.features.transactions.application.DeleteTransactionsUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacao")
@Tag(name = "Transações", description = "Endpoint para o gerenciamento de transações financeiras")
public class TransactionController {
    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);
    private final CreateTransactionUseCase createTransactionUseCase;
    private final DeleteTransactionsUseCase deleteTransactionsUseCase;

    public TransactionController(CreateTransactionUseCase createTransactionUseCase, DeleteTransactionsUseCase deleteTransactionsUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
        this.deleteTransactionsUseCase = deleteTransactionsUseCase;
    }

    @Operation(summary = "Registra uma nova transação",
            description = "Recebe um valor e uma data para processar uma transação"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transação aceitada e registrada"),
            @ApiResponse(responseCode = "422", description = "Transação rejeitada (Valor negativo ou data ausente)."),
            @ApiResponse(responseCode = "400", description = "Requisição inválida (JSON mal formatado)")
    })
    @PostMapping
    public ResponseEntity<Void> register(@RequestBody @Validated CreateTransactionRequest request) {
        log.info("Recebida requisição para criar transação: valor={}, dataHora={}", request.valor(), request.dataHora());
        createTransactionUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Limpa todas as transações persistidas em memória")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todas as transações foram apagadas!")
    })
    @DeleteMapping
    public ResponseEntity<Void> clear() {
        log.info("Recebida requisição para limpar todas as transações");
        deleteTransactionsUseCase.execute();
        return ResponseEntity.ok().build();
    }
}
