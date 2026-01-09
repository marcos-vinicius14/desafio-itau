package marcosvinicius.desafioitau.domain.transaction;

import marcosvinicius.desafioitau.domain.exceptions.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Transaction")
class TransactionTest {

    @Nested
    @DisplayName("Criação de transação")
    class Creation {

        @Test
        @DisplayName("deve criar transação com valores válidos")
        void shouldCreateTransactionWithValidValues() {
            BigDecimal amount = new BigDecimal("100.50");
            OffsetDateTime date = OffsetDateTime.now().minusMinutes(5);

            Transaction transaction = new Transaction(amount, date);

            assertNotNull(transaction.getId());
            assertEquals(amount, transaction.getAmount());
            assertEquals(date, transaction.getDate());
        }

        @Test
        @DisplayName("deve lançar exceção quando valor é nulo")
        void shouldThrowExceptionWhenAmountIsNull() {
            OffsetDateTime date = OffsetDateTime.now().minusMinutes(5);

            DomainException exception = assertThrows(DomainException.class,
                    () -> new Transaction(null, date));

            assertEquals("O valor da transação é obrigatório", exception.getMessage());
        }

        @Test
        @DisplayName("deve lançar exceção quando valor é negativo")
        void shouldThrowExceptionWhenAmountIsNegative() {
            BigDecimal negativeAmount = new BigDecimal("-50.00");
            OffsetDateTime date = OffsetDateTime.now().minusMinutes(5);

            DomainException exception = assertThrows(DomainException.class,
                    () -> new Transaction(negativeAmount, date));

            assertEquals("O valor da transação deve ser positivo", exception.getMessage());
        }

        @Test
        @DisplayName("deve aceitar valor igual a zero")
        void shouldAcceptZeroAmount() {
            BigDecimal zero = BigDecimal.ZERO;
            OffsetDateTime date = OffsetDateTime.now().minusMinutes(5);

            Transaction transaction = new Transaction(zero, date);

            assertEquals(BigDecimal.ZERO, transaction.getAmount());
        }

        @Test
        @DisplayName("deve lançar exceção quando data é nula")
        void shouldThrowExceptionWhenDateIsNull() {
            BigDecimal amount = new BigDecimal("100.00");

            DomainException exception = assertThrows(DomainException.class,
                    () -> new Transaction(amount, null));

            assertEquals("A data da transação é obrigatória!", exception.getMessage());
        }

        @Test
        @DisplayName("deve lançar exceção quando data está no futuro")
        void shouldThrowExceptionWhenDateIsInFuture() {
            BigDecimal amount = new BigDecimal("100.00");
            OffsetDateTime futureDate = OffsetDateTime.now().plusMinutes(5);

            DomainException exception = assertThrows(DomainException.class,
                    () -> new Transaction(amount, futureDate));

            assertEquals("A data da transação não pode estar no futuro", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Verificação de tempo")
    class TimeCheck {

        @Test
        @DisplayName("deve retornar true quando transação ocorreu nos últimos N segundos")
        void shouldReturnTrueWhenTransactionHappenedInLastNSeconds() {
            BigDecimal amount = new BigDecimal("100.00");
            OffsetDateTime date = OffsetDateTime.now().minusSeconds(30);

            Transaction transaction = new Transaction(amount, date);

            assertTrue(transaction.happenedInTheLast(60L));
        }

        @Test
        @DisplayName("deve retornar false quando transação é mais antiga que N segundos")
        void shouldReturnFalseWhenTransactionIsOlderThanNSeconds() {
            BigDecimal amount = new BigDecimal("100.00");
            OffsetDateTime date = OffsetDateTime.now().minusSeconds(90);

            Transaction transaction = new Transaction(amount, date);

            assertFalse(transaction.happenedInTheLast(60L));
        }
    }

    @Nested
    @DisplayName("Igualdade e HashCode")
    class EqualityAndHashCode {

        @Test
        @DisplayName("deve ser igual a si mesmo")
        void shouldBeEqualToItself() {
            BigDecimal amount = new BigDecimal("100.00");
            OffsetDateTime date = OffsetDateTime.now().minusMinutes(5);

            Transaction transaction = new Transaction(amount, date);

            assertEquals(transaction, transaction);
        }

        @Test
        @DisplayName("transações diferentes devem ter IDs diferentes")
        void differentTransactionsShouldHaveDifferentIds() {
            BigDecimal amount = new BigDecimal("100.00");
            OffsetDateTime date = OffsetDateTime.now().minusMinutes(5);

            Transaction t1 = new Transaction(amount, date);
            Transaction t2 = new Transaction(amount, date);

            assertNotEquals(t1, t2);
            assertNotEquals(t1.getId(), t2.getId());
        }
    }
}
