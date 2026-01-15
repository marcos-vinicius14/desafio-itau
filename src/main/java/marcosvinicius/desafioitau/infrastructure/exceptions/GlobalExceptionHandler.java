package marcosvinicius.desafioitau.infrastructure.exceptions;

import marcosvinicius.desafioitau.domain.exceptions.DomainException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException exception) {
        log.warn("Violação de regra de domínio: {}", exception.getMessage());
        ErrorResponse error = new ErrorResponse(422, "Unprocessable Entity", exception.getMessage());
        return ResponseEntity.unprocessableEntity().body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.error("Erro na leitura da mensagem HTTP: {}", exception.getMessage());
        ErrorResponse error = new ErrorResponse(400, "Bad Request", "JSON mal formatado ou inválido");
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.warn("Erro de validação de argumento: {}", exception.getMessage());
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .findFirst()
                .orElse("Erro de validação");
        ErrorResponse error = new ErrorResponse(400, "Bad Request", message);
        return ResponseEntity.badRequest().body(error);
    }
}
