package marcosvinicius.desafioitau.features.transactions.api;

import marcosvinicius.desafioitau.features.transactions.application.CreateTransactionUseCase;
import marcosvinicius.desafioitau.features.transactions.application.DeleteTransactionsUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacao")
public class TransactionController {
    private final CreateTransactionUseCase createTransactionUseCase;
    private final DeleteTransactionsUseCase deleteTransactionsUseCase;

    public TransactionController(CreateTransactionUseCase createTransactionUseCase, DeleteTransactionsUseCase deleteTransactionsUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
        this.deleteTransactionsUseCase = deleteTransactionsUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody @Validated CreateTransactionRequest request) {
        createTransactionUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clear() {
        deleteTransactionsUseCase.execute();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
