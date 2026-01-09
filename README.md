# 🏦 Desafio Itaú - API de Transações

API REST para gerenciamento de transações financeiras e cálculo de estatísticas em tempo real.

---

## 📋 Índice

- [Visão Geral](#-visão-geral)
- [Tecnologias](#-tecnologias)
- [Arquitetura](#-arquitetura)
- [Decisões Técnicas](#-decisões-técnicas)
- [Endpoints](#-endpoints)
- [Como Executar](#-como-executar)
- [Testes](#-testes)
- [Estrutura do Projeto](#-estrutura-do-projeto)

---

## 🎯 Visão Geral

Esta API permite:
- **Registrar transações** com valor e data/hora
- **Calcular estatísticas** (soma, média, mínimo, máximo, contagem) das transações dos últimos 60 segundos
- **Limpar todas as transações** armazenadas

### Regras de Negócio

1. Transações com **data no futuro** são rejeitadas (HTTP 422)
2. Transações com **valor negativo** são rejeitadas (HTTP 422)
3. Estatísticas consideram apenas transações dos **últimos 60 segundos**
4. Armazenamento é **em memória** (não persiste após reinício)

---

## 🛠 Tecnologias

| Tecnologia | Versão | Motivo |
|------------|--------|--------|
| **Java** | 21 | LTS mais recente, suporte a Virtual Threads e Records |
| **Spring Boot** | 3.4.1 | Framework maduro, produtivo e com grande ecossistema |
| **Gradle** | 9.2.1 | Build mais rápido que Maven, DSL Kotlin nativo |
| **Lombok** | - | Reduz boilerplate (getters, construtores) |
| **SpringDoc OpenAPI** | 2.8.1 | Documentação Swagger automática |
| **JUnit 5** | - | Testes modernos com suporte a nested classes |
| **Docker** | Multi-stage | Imagem otimizada e segura |

---

## 🏗 Arquitetura

A aplicação segue princípios de **Clean Architecture** e **Domain-Driven Design (DDD)**:

```
┌─────────────────────────────────────────────────────────────┐
│                        API Layer                            │
│  (Controllers, DTOs - Request/Response)                     │
├─────────────────────────────────────────────────────────────┤
│                    Application Layer                        │
│  (Use Cases - orquestração de regras de negócio)            │
├─────────────────────────────────────────────────────────────┤
│                      Domain Layer                           │
│  (Entities, Value Objects, Repository Interfaces)           │
├─────────────────────────────────────────────────────────────┤
│                   Infrastructure Layer                      │
│  (Implementações de Repository, Exception Handlers)         │
└─────────────────────────────────────────────────────────────┘
```

### Fluxo de Dados

```
HTTP Request → Controller → UseCase → Repository → Domain
                    ↓            ↓
              Validation    Business Logic
```

---

## 💡 Decisões Técnicas

### 1. Separação em Camadas (Clean Architecture)

**Decisão**: Separar a aplicação em camadas distintas (API, Application, Domain, Infrastructure).

**Motivo**: 
- Facilita testes unitários isolados
- Permite trocar implementações (ex: banco de dados) sem afetar o domínio
- Código mais organizado e manutenível

---

### 2. Use Cases como Classes Separadas

**Decisão**: Cada operação de negócio é uma classe própria (`CreateTransactionUseCase`, `DeleteTransactionsUseCase`, `GetStatisticsUseCase`).

**Motivo**:
- Respeita o **Single Responsibility Principle (SRP)**
- Facilita testes unitários
- Cada classe tem uma única razão para mudar

---

### 3. Repository Pattern

**Decisão**: Interface `TransactionRepository` no domínio, implementação `InMemoryTransactionRepository` na infraestrutura.

**Motivo**:
- O domínio não depende de detalhes de persistência
- Facilita trocar para banco de dados real no futuro
- Inversão de dependência (DIP)

---

### 4. Validação em Duas Camadas

**Decisão**: Validação com Bean Validation na API + validação de negócio no domínio.

**Motivo**:
- **API (Bean Validation)**: Falha rápida para requests mal formados (HTTP 400)
- **Domínio**: Protege invariantes de negócio (HTTP 422)
- Defesa em profundidade

---

### 5. Entidade Rica (Rich Domain Model)

**Decisão**: A classe `Transaction` encapsula suas próprias validações.

**Motivo**:
- Impossível criar uma `Transaction` inválida
- Lógica de negócio junto aos dados
- Evita "Anemic Domain Model"

```java
public Transaction(BigDecimal amount, OffsetDateTime date) {
    ensurePositiveValue(amount);  // Valida antes de criar
    ensureNotFuture(date);
    // ...
}
```

---

### 6. @Service em vez de @Configuration + @Bean

**Decisão**: Use Cases anotados com `@Service` diretamente.

**Motivo**:
- Menos arquivos de configuração
- Injeção de dependência automática pelo Spring
- Código mais simples e idiomático

---

### 7. Logging com SLF4J

**Decisão**: Logs em Controllers, Use Cases e Exception Handlers.

**Motivo**:
- Rastreabilidade de requisições
- Debug facilitado em produção
- Integração com sistemas de monitoramento (ELK, Grafana, etc.)

---

### 8. DoubleSummaryStatistics para Cálculo

**Decisão**: Usar `DoubleSummaryStatistics` do Java para calcular estatísticas.

**Motivo**:
- API nativa do Java, otimizada
- Calcula sum, avg, min, max, count em uma única passagem
- Requisito do desafio

---

### 9. Collections.synchronizedSet para Thread-Safety

**Decisão**: Usar `Collections.synchronizedSet(new LinkedHashSet<>())` para armazenar transações.

**Motivo**:
- Thread-safe para operações básicas
- `LinkedHashSet` mantém ordem de inserção
- Suficiente para o escopo do desafio

---

### 10. Docker Multi-Stage Build

**Decisão**: Dockerfile com estágios de build e runtime separados.

**Motivo**:
- Imagem final menor (~200MB vs ~700MB)
- Não expõe ferramentas de build na imagem de produção
- Segurança melhorada

```dockerfile
FROM gradle:jdk21-alpine AS builder  # Stage 1: Build
FROM eclipse-temurin:21-jre-alpine   # Stage 2: Runtime
```

---

## 🌐 Endpoints

### POST /transacao
Registra uma nova transação.

**Request Body:**
```json
{
  "valor": 100.50,
  "dataHora": "2024-01-09T15:30:00.000-03:00"
}
```

**Responses:**
| Status | Descrição |
|--------|-----------|
| 201 | Transação registrada com sucesso |
| 400 | JSON inválido ou campos obrigatórios ausentes |
| 422 | Valor negativo ou data no futuro |

---

### GET /estatistica
Retorna estatísticas das transações dos últimos 60 segundos.

**Response:**
```json
{
  "count": 10,
  "sum": 1500.00,
  "avg": 150.00,
  "min": 50.00,
  "max": 300.00
}
```

---

### DELETE /transacao
Remove todas as transações.

**Response:** `200 OK`

---

## 🚀 Como Executar

### Pré-requisitos
- Java 21+
- Docker (opcional)

### Localmente

```bash
# Clonar o repositório
git clone <repo-url>
cd desafio-itau

# Executar
./gradlew bootRun
```

A API estará disponível em `http://localhost:8080`

### Com Docker

```bash
# Build e execução
docker compose up --build

# Ou manualmente
docker build -t desafio-itau .
docker run -p 8080:8080 desafio-itau
```

### Swagger UI

Documentação interativa disponível em:
```
http://localhost:8080/swagger-ui.html
```

---

## 🧪 Testes

### Executar Testes

```bash
./gradlew test
```

### Cobertura

| Tipo | Quantidade |
|------|------------|
| Unitários (Domain) | 18 |
| Integração (API) | 13 |
| Smoke | 1 |
| **Total** | **32** |

### Casos de Teste

- ✅ Criação de transação válida
- ✅ Rejeição de valor negativo
- ✅ Rejeição de data futura
- ✅ Cálculo de estatísticas
- ✅ Filtro de transações antigas (> 60s)
- ✅ Limpeza de transações
- ✅ Validação de campos obrigatórios
- ✅ Tratamento de JSON inválido

---

## 📁 Estrutura do Projeto

```
src/main/java/marcosvinicius/desafioitau/
├── DesafioItauApplication.java          # Ponto de entrada
├── domain/                               # Camada de Domínio
│   ├── Statistics.java                   # Value Object
│   ├── exceptions/
│   │   └── DomainException.java
│   └── transaction/
│       ├── Transaction.java              # Entidade
│       ├── TransactionRepository.java    # Interface
│       └── Transactions.java             # Coleção de domínio
├── features/                             # Vertical Slices
│   ├── statistics/
│   │   ├── api/
│   │   │   ├── StatisticsController.java
│   │   │   └── StatisticsResponse.java
│   │   └── application/
│   │       └── GetStatisticsUseCase.java
│   └── transactions/
│       ├── api/
│       │   ├── CreateTransactionRequest.java
│       │   └── TransactionController.java
│       └── application/
│           ├── CreateTransactionUseCase.java
│           └── DeleteTransactionsUseCase.java
└── infrastructure/                       # Camada de Infraestrutura
    ├── exceptions/
    │   └── GlobalExceptionHandler.java
    └── persistence/
        └── InMemoryTransactionRepository.java
```

---

## 📄 Licença

Este projeto foi desenvolvido como parte de um desafio técnico.

---

## 👤 Autor

**Marcos Vinícius**  
📧 marcos.vinicius.dev3@gmail.com
