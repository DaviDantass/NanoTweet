# NanoTweet 

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)

Uma rede social minimalista construída com **Spring Boot**, onde cada post tem até **42 caracteres**.
Projeto focado em arquitetura limpa, boas práticas e stack moderna Java.

## Tecnologias Utilizadas

### **Backend**

* **Java 21**
* **Spring Boot 3.3**
* **Spring Web**
* **Spring Data JPA**
* **Bean Validation**

### **Banco & Migração**

* **MySQL 8** (produção)
* **H2** (testes)
* **Flyway** (migrações)

### **Testes**

* **JUnit 5**
* **Mockito**
* **MockMvc**
* testes cobrindo services e controllers

### **DevOps / Infra**

* **Docker** + **Docker Compose**
* Multi-stage build
* Volume persistente para MySQL
* Healthchecks

### **Build**

* **Maven**

---

## Funcionalidades

* CRUD de usuários
* Posts de 1–42 caracteres
* Repost e Quote
* Feed público ordenado
* Filtro por autor
* Validações de entrada
* Tratamento de erros padronizado

---

## Como Executar

### Via Docker (recomendado)

```bash
docker-compose up --build
```

### Local

```bash
./mvnw spring-boot:run
```

---

## Exemplos de API

Criar usuário:

```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"username": "davidantas"}'
```

Criar post:

```bash
curl -X POST "http://localhost:8080/posts?authorId=1" \
  -H "Content-Type: application/json" \
  -d '{"content": "Hello 42!"}'
```

---

## Arquitetura

```
Controller → Service → Repository
      DTOs → Regras → Entidades
```

---


## Autor

GitHub: [@DaviDantass](https://github.com/DaviDantass)

