package marcosvinicius.desafioitau.domain.transaction;

import marcosvinicius.desafioitau.domain.exceptions.DomainException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;


public final class Transaction {
    private final UUID id;
    private final BigDecimal amount;
    private final OffsetDateTime date;

    public Transaction(BigDecimal amount, OffsetDateTime date) {
        ensurePositiveValue(amount);
        ensureNotFuture(date);

        this.amount = amount;
        this.date = date;
        this.id = UUID.randomUUID();
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public OffsetDateTime getDate() {
        return date;
    }

    public UUID getId() {
        return id;
    }

    private void ensurePositiveValue(BigDecimal amount) {
        if (amount == null) {
            throw new DomainException("O valor da transação é obrigatório");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("O valor da transação deve ser positivo");
        }
    }

    private void ensureNotFuture(OffsetDateTime date) {
        if (date == null) {
            throw new DomainException("A data da transação é obrigatória!");
        }

        if (date.isAfter(OffsetDateTime.now())) {
            throw new DomainException("A data da transação não pode estar no futuro");
        }
    }

    public boolean happenedInTheLast(Long seconds) {
        return date.isBefore(OffsetDateTime.now().minusSeconds(seconds));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Transaction{id=" + id + ", amount=" + amount + ", date=" + date + '}';
    }


}
