# Build stage
FROM maven:3.9.5-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copiar arquivos de configuração do Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Baixar dependências (layer cacheável)
RUN mvn dependency:go-offline -B

# Copiar código fonte
COPY src src

# Build da aplicação (skip tests para build mais rápido)
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Criar usuário não-root para segurança
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar JAR do build stage
COPY --from=build /app/target/*.jar app.jar

# Expor porta da aplicação
EXPOSE 8080

# Configurar healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Executar aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
