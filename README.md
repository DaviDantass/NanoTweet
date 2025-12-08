# NanoTweet 🐦

> Uma rede social minimalista construída com Spring Boot - Pense antes de tweetar, você tem apenas 42 caracteres!

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.3-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

## 📋 Sobre o Projeto

NanoTweet é uma aplicação de rede social simplificada que implementa funcionalidades essenciais de microblogging com limite de 42 caracteres por post. O projeto demonstra boas práticas de desenvolvimento com Spring Boot, incluindo arquitetura MVC limpa, testes automatizados e containerização com Docker.

### ✨ Funcionalidades

- **Gestão de Usuários**: CRUD completo com validação
- **Posts**: Criar posts originais (1-42 caracteres)
- **Repost**: Compartilhar posts de outros usuários
- **Quote**: Citar posts com comentário
- **Feed**: Visualização cronológica de todos os posts
- **Filtros**: Buscar posts por autor
- **Validações**: Bean Validation em todas as entradas
- **Tratamento de Erros**: Respostas HTTP consistentes e informativas

## 🏗️ Arquitetura

```
Controller → Service → Repository → Database
     ↓          ↓          ↓
   DTOs    Business    Entities
            Logic
```

### Tecnologias Utilizadas

- **Backend**: Spring Boot 3.3.3, Spring Data JPA, Spring Validation
- **Banco de Dados**: MySQL 8.0 (produção), H2 (testes)
- **Migrações**: Flyway
- **Testes**: JUnit 5, Mockito, MockMvc
- **Build**: Maven
- **Containerização**: Docker, Docker Compose

## 🚀 Como Executar

### Pré-requisitos

- Java 21+
- Maven 3.9+
- Docker e Docker Compose (opcional)

### Opção 1: Docker (Recomendado)

```bash
# Subir toda a aplicação (banco + app)
docker-compose up --build

# A aplicação estará disponível em http://localhost:8080
```

### Opção 2: Local (MySQL local)

1. **Iniciar o banco MySQL** (via Docker):
```bash
docker-compose up db
```

2. **Executar a aplicação**:
```bash
./mvnw spring-boot:run
```

### Opção 3: Desenvolvimento (com IDE)

1. Importar o projeto na sua IDE favorita
2. Configurar banco de dados em `application.properties`
3. Executar `NanoTweetApplication.java`

## 🧪 Testes

```bash
# Executar todos os testes
./mvnw test

# Executar com coverage
./mvnw test jacoco:report
```

**Cobertura de Testes**: 41 testes (21 unit + 20 integration)
- UserService: 9 testes
- PostService: 12 testes
- UserController: 7 testes
- PostController: 12 testes
- FeedController: 1 teste

## 📡 API Endpoints

### Usuários

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/users` | Listar todos os usuários |
| GET | `/users/{id}` | Buscar usuário por ID |
| POST | `/users` | Criar novo usuário |
| PUT | `/users/{id}` | Atualizar usuário |
| DELETE | `/users/{id}` | Deletar usuário |

**Exemplo - Criar usuário**:
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"username": "davidantas"}'
```

### Posts

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/posts` | Listar todos os posts |
| GET | `/posts/{id}` | Buscar post por ID |
| GET | `/posts/author/{authorId}` | Posts de um autor |
| POST | `/posts?authorId={id}` | Criar post original |
| POST | `/posts/{id}/repost?authorId={id}` | Fazer repost |
| POST | `/posts/{id}/quote?authorId={id}` | Citar post |
| DELETE | `/posts/{id}` | Deletar post |

**Exemplo - Criar post**:
```bash
curl -X POST "http://localhost:8080/posts?authorId=1" \
  -H "Content-Type: application/json" \
  -d '{"content": "Ola mundo em 42 caracteres!"}'
```

**Exemplo - Repost**:
```bash
curl -X POST "http://localhost:8080/posts/1/repost?authorId=2"
```

**Exemplo - Quote**:
```bash
curl -X POST "http://localhost:8080/posts/1/quote?authorId=2" \
  -H "Content-Type: application/json" \
  -d '{"content": "Concordo totalmente!"}'
```

### Feed

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/feed` | Feed público ordenado por data |

## 🗂️ Estrutura do Projeto

```
NanoTweet/
├── src/
│   ├── main/
│   │   ├── java/com/davidantasdev/NanoTweet/
│   │   │   ├── controllers/         # REST Controllers
│   │   │   ├── service/              # Business Logic
│   │   │   ├── repository/           # Data Access
│   │   │   ├── model/                # Entities & DTOs
│   │   │   ├── exception/            # Exception Handling
│   │   │   └── NanoTweetApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/         # Flyway migrations
│   └── test/                          # Testes
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```

## 🔧 Configuração

### Variáveis de Ambiente

| Variável | Padrão | Descrição |
|----------|--------|-----------|
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://localhost:3306/NanoTweet` | URL do banco |
| `SPRING_DATASOURCE_USERNAME` | `root` | Usuário do banco |
| `SPRING_DATASOURCE_PASSWORD` | *(vazio)* | Senha do banco |
| `SERVER_PORT` | `8080` | Porta da aplicação |
| `SPRING_JPA_SHOW_SQL` | `false` | Mostrar SQL no log |

### Docker Compose

O arquivo `docker-compose.yml` configura:
- **MySQL 8.0** na porta 3307 (host) → 3306 (container)
- **App Spring Boot** na porta 8080
- **Network** isolada para comunicação entre serviços
- **Volume** persistente para dados do MySQL
- **Healthcheck** para garantir ordem de inicialização

## 📊 Modelo de Dados

### User
```java
{
  "id": 1,
  "username": "davidantas",    // 1-10 caracteres, alfanumérico + _
  "createdAt": "2025-12-08T12:00:00"
}
```

### Post
```java
{
  "id": 1,
  "content": "Ola mundo!",     // 0-42 caracteres (vazio para repost)
  "authorUsername": "davidantas",
  "type": "ORIGINAL",          // ORIGINAL | REPOST | QUOTE
  "originalPostId": null,      // ID do post original (repost/quote)
  "createdAt": "2025-12-08T12:00:00"
}
```

## 🛡️ Segurança & Boas Práticas

✅ **Validação de entrada** com Bean Validation  
✅ **Transações** com `@Transactional`  
✅ **Separação de responsabilidades** (Controller/Service/Repository)  
✅ **DTOs** para desacoplar API de entidades  
✅ **Exception Handling** centralizado com `@RestControllerAdvice`  
✅ **Migrações versionadas** com Flyway  
✅ **Testes automatizados** (unit + integration)  
✅ **Docker multi-stage build** para otimização  
✅ **Usuário não-root** nos containers  
✅ **Healthchecks** para resiliência  

## 🐛 Troubleshooting

### Erro de conexão com MySQL
```bash
# Verificar se o MySQL está rodando
docker-compose ps

# Ver logs do banco
docker-compose logs db
```

### Porta 8080 já em uso
```bash
# Usar variável de ambiente
SERVER_PORT=8081 ./mvnw spring-boot:run
```

### Testes falhando
```bash
# Limpar e recompilar
./mvnw clean test
```

## 📝 Próximos Passos

- [ ] Implementar autenticação JWT
- [ ] Adicionar paginação nos endpoints
- [ ] Sistema de likes/curtidas
- [ ] Seguir/deixar de seguir usuários
- [ ] Feed personalizado por usuário
- [ ] Upload de imagens
- [ ] API de busca por hashtags
- [ ] Métricas com Spring Actuator
- [ ] Cache com Redis
- [ ] Deploy em cloud (AWS/Azure/GCP)

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👨‍💻 Autor

**David Antas**
- GitHub: [@DaviDantass](https://github.com/DaviDantass)

---

⭐ Se este projeto foi útil para você, considere dar uma estrela!

**Desenvolvido com ☕ e Spring Boot**
