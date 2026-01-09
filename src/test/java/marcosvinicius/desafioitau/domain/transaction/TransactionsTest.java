package marcosvinicius.desafioitau.domain.transaction;

import marcosvinicius.desafioitau.domain.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Transactions")
class TransactionsTest {

    private Transactions transactions;

    @BeforeEach
    void setUp() {
        transactions = new Transactions();
    }

    @Nested
    @DisplayName("Adicionar transações")
    class AddTransactions {

        @Test
        @DisplayName("deve adicionar transação com sucesso")
        void shouldAddTransactionSuccessfully() {
            Transaction transaction = new Transaction(
                    new BigDecimal("100.00"),
                    OffsetDateTime.now().minusSeconds(30)
            );

            transactions.add(transaction);

            assertEquals(1, transactions.getAll().size());
            assertTrue(transactions.getAll().contains(transaction));
        }

        @Test
        @DisplayName("não deve adicionar transação nula")
        void shouldNotAddNullTransaction() {
            transactions.add(null);

            assertEquals(0, transactions.getAll().size());
        }
    }

    @Nested
    @DisplayName("Limpar transações")
    class ClearTransactions {

        @Test
        @DisplayName("deve limpar todas as transações")
        void shouldClearAllTransactions() {
            transactions.add(new Transaction(new BigDecimal("100.00"), OffsetDateTime.now().minusSeconds(30)));
            transactions.add(new Transaction(new BigDecimal("200.00"), OffsetDateTime.now().minusSeconds(20)));

            transactions.clear();

            assertEquals(0, transactions.getAll().size());
        }
    }

    @Nested
    @DisplayName("Calcular estatísticas dos últimos 60 segundos")
    class CalculateStatistics {

        @Test
        @DisplayName("deve retornar estatísticas zeradas quando não há transações")
        void shouldReturnZeroStatisticsWhenNoTransactions() {
            Statistics stats = transactions.calculateLast60Seconds();

            assertEquals(0L, stats.count());
            assertEquals(BigDecimal.ZERO, stats.sum());
            assertEquals(BigDecimal.ZERO, stats.avg());
            assertEquals(BigDecimal.ZERO, stats.min());
            assertEquals(BigDecimal.ZERO, stats.max());
        }

        @Test
        @DisplayName("deve calcular estatísticas corretamente com uma transação")
        void shouldCalculateStatisticsWithOneTransaction() {
            BigDecimal amount = new BigDecimal("150.00");
            transactions.add(new Transaction(amount, OffsetDateTime.now().minusSeconds(30)));

            Statistics stats = transactions.calculateLast60Seconds();

            assertEquals(1L, stats.count());
            assertEquals(new BigDecimal("150.00"), stats.sum());
            assertEquals(new BigDecimal("150.00"), stats.avg());
            assertEquals(new BigDecimal("150.00"), stats.min());
            assertEquals(new BigDecimal("150.00"), stats.max());
        }

        @Test
        @DisplayName("deve calcular estatísticas corretamente com múltiplas transações")
        void shouldCalculateStatisticsWithMultipleTransactions() {
            transactions.add(new Transaction(new BigDecimal("100.00"), OffsetDateTime.now().minusSeconds(10)));
            transactions.add(new Transaction(new BigDecimal("200.00"), OffsetDateTime.now().minusSeconds(20)));
            transactions.add(new Transaction(new BigDecimal("300.00"), OffsetDateTime.now().minusSeconds(30)));

            Statistics stats = transactions.calculateLast60Seconds();

            assertEquals(3L, stats.count());
            assertEquals(new BigDecimal("600.00"), stats.sum());
            assertEquals(new BigDecimal("200.00"), stats.avg());
            assertEquals(new BigDecimal("100.00"), stats.min());
            assertEquals(new BigDecimal("300.00"), stats.max());
        }

        @Test
        @DisplayName("deve ignorar transações mais antigas que 60 segundos")
        void shouldIgnoreTransactionsOlderThan60Seconds() {
            transactions.add(new Transaction(new BigDecimal("100.00"), OffsetDateTime.now().minusSeconds(30)));
            transactions.add(new Transaction(new BigDecimal("500.00"), OffsetDateTime.now().minusSeconds(90)));

            Statistics stats = transactions.calculateLast60Seconds();

            assertEquals(1L, stats.count());
            assertEquals(new BigDecimal("100.00"), stats.sum());
        }

        @Test
        @DisplayName("deve retornar estatísticas zeradas quando todas transações são antigas")
        void shouldReturnZeroStatisticsWhenAllTransactionsAreOld() {
            transactions.add(new Transaction(new BigDecimal("100.00"), OffsetDateTime.now().minusSeconds(90)));
            transactions.add(new Transaction(new BigDecimal("200.00"), OffsetDateTime.now().minusSeconds(120)));

            Statistics stats = transactions.calculateLast60Seconds();

            assertEquals(0L, stats.count());
            assertEquals(BigDecimal.ZERO, stats.sum());
        }
    }

    @Nested
    @DisplayName("Imutabilidade da coleção retornada")
    class CollectionImmutability {

        @Test
        @DisplayName("deve retornar coleção imutável")
        void shouldReturnImmutableCollection() {
            transactions.add(new Transaction(new BigDecimal("100.00"), OffsetDateTime.now().minusSeconds(30)));

            assertThrows(UnsupportedOperationException.class, () ->
                    transactions.getAll().add(new Transaction(new BigDecimal("200.00"), OffsetDateTime.now().minusSeconds(20)))
            );
        }
    }
}
