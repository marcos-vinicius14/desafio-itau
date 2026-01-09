package marcosvinicius.desafioitau.features.transactions.api;

import marcosvinicius.desafioitau.features.transactions.application.RegisterTransactionUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacao")
public class TransactionController {
    private final RegisterTransactionUseCase useCase;

    public TransactionController(RegisterTransactionUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody @Validated TransactionRequest request) {
        useCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
