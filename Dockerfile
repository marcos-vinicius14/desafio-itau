# ==============================================================================
# ESTÁGIO 1: BUILD
# ==============================================================================
# Mudamos de 'gradle:8.5-...' para 'gradle:jdk21-alpine'
# Isso garante que ele baixe a versão ESTÁVEL MAIS RECENTE do Gradle
FROM gradle:jdk21-alpine AS builder

WORKDIR /app

# 1. Copiamos todo o projeto
COPY . .

# 2. Executamos o build
# O '--no-daemon' é crucial para não travar o processo no final
RUN gradle clean bootJar -x test --no-daemon

# ==============================================================================
# ESTÁGIO 2: RUNTIME
# ==============================================================================
FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S spring && adduser -S spring -G spring
WORKDIR /app

# 3. Copia o JAR gerado (Pode variar entre -plain.jar ou snapshot)
# Usamos find/cp para garantir que pegamos o arquivo certo se o nome variar
COPY --from=builder /app/build/libs/*.jar app.jar

USER spring
EXPOSE 8080
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]