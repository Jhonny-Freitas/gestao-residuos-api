# Projeto - Cidades ESG Inteligentes

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Oracle](https://img.shields.io/badge/Oracle-21c-red.svg)](https://www.oracle.com/database/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)
[![CI/CD](https://img.shields.io/badge/CI%2FCD-GitHub%20Actions-black.svg)](https://github.com/features/actions)

**Aluno:** Jhonny Miguel de Freitas
**Curso:** FIAP - Trilha Frameworks Java / SQL
**Tema ESG:** Environmental — Gestao de Residuos e Reciclagem

---

## Sobre o Projeto

API RESTful desenvolvida com Spring Boot para gerenciamento inteligente de residuos reciclaveis, alinhada aos principios ESG (Environmental, Social, and Governance). O sistema automatiza o rastreamento de residuos, gestao de containers, agendamento de coletas e geracao de alertas automaticos via triggers Oracle.

---

## Como executar localmente com Docker

### Pre-requisitos

- [Docker](https://www.docker.com/) instalado
- [Docker Compose](https://docs.docker.com/compose/) instalado
- Acesso ao Oracle Container Registry (para pull da imagem Oracle 21c XE)

### Passo a passo

**1. Clone o repositorio**

```bash
git clone https://github.com/seu-usuario/gestao-residuos-api.git
cd gestao-residuos-api
```

**2. Configure as variaveis de ambiente**

```bash
cp .env.example .env
# Edite o .env se quiser alterar senhas ou portas
```

**3. Suba o ambiente padrao (producao local)**

```bash
docker-compose up -d
```

**4. Aguarde a inicializacao do Oracle (~2 minutos)**

```bash
docker-compose logs -f oracle-db
# Aguarde a mensagem: "DATABASE IS READY TO USE!"
```

**5. Acesse a aplicacao**

| Servico    | URL                                      |
|------------|------------------------------------------|
| API REST   | http://localhost:8080                    |
| Swagger UI | http://localhost:8080/swagger-ui.html    |
| API Docs   | http://localhost:8080/api-docs           |

**6. Autenticacao**

```bash
POST /api/auth/login
{
  "email": "admin@fiap.com.br",
  "senha": "admin123"
}
```

Use o token retornado no header: `Authorization: Bearer {token}`

### Subindo ambiente de staging

```bash
docker-compose -f docker-compose.staging.yml up -d
# Disponivel em: http://localhost:8081/swagger-ui.html
```

### Parando os containers

```bash
# Ambiente padrao
docker-compose down

# Staging
docker-compose -f docker-compose.staging.yml down
```

---

## Pipeline CI/CD

### Ferramenta utilizada

**GitHub Actions** — configurado em `.github/workflows/ci-cd.yml`

### Estrategia de branches

| Branch    | Ambiente    | Trigger          |
|-----------|-------------|------------------|
| `develop` | Staging     | Push automatico  |
| `main`    | Production  | Push + aprovacao |

### Etapas do pipeline

```
push → develop / main
       │
       ▼
  ┌─────────┐
  │  BUILD  │  mvn clean package -DskipTests
  └────┬────┘
       │
       ▼
  ┌─────────┐
  │  TEST   │  mvn test (testes unitarios)
  └────┬────┘
       │
       ├──── branch develop ──▶  ┌─────────────────┐
       │                          │ DEPLOY STAGING  │ porta 8081
       │                          │ tag: :staging   │
       │                          └─────────────────┘
       │
       └──── branch main ────▶  ┌──────────────────────┐
                                  │  DEPLOY PRODUCTION   │ porta 8080
                                  │  tag: :latest        │
                                  │  (aprovacao manual)  │
                                  └──────────────────────┘
```

### Detalhes de cada etapa

**Build**
- Checkout do codigo
- Configuracao do Java 21 (Temurin) com cache Maven
- `mvn clean package -DskipTests`
- Upload do JAR como artefato do workflow

**Test**
- Execucao dos testes unitarios: `mvn test`
- Upload do relatorio Surefire como artefato (retencao: 7 dias)
- Testes rodam sem banco de dados (sem dependencia de infraestrutura)

**Deploy Staging** (branch `develop`)
- Login no GitHub Container Registry (ghcr.io)
- Build e push da imagem com tags `:staging` e `:staging-{sha}`
- Confirmacao do deploy com instrucoes de execucao

**Deploy Production** (branch `main`)
- Requer aprovacao manual via GitHub Environments
- Login no GitHub Container Registry (ghcr.io)
- Build e push da imagem com tags `:latest`, `:production` e `:prod-{sha}`
- Confirmacao do deploy com instrucoes de execucao

### Configurar aprovacao manual para producao

No GitHub, va em: **Settings > Environments > production > Required reviewers**
Adicione seu usuario como revisor obrigatorio.

---

## Containerizacao

### Dockerfile

```dockerfile
# Stage 1: Build com Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime minimo com JRE Alpine
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Estrategias adotadas

**Multi-stage build**
- Stage 1 usa `maven:3.9.6-eclipse-temurin-21` para compilar — imagem grande, mas descartada apos build
- Stage 2 usa `eclipse-temurin:21-jre-alpine` (JRE minimo) — imagem final leve (~200MB vs ~600MB)

**Cache de dependencias Maven**
- `RUN mvn dependency:go-offline` baixa deps antes de copiar o src
- Reuso do layer de cache enquanto `pom.xml` nao mudar

**Configuracao por variaveis de ambiente**
- Sem credenciais hardcoded na imagem
- `SPRING_PROFILES_ACTIVE` define qual `application-{profile}.properties` carregar

### Orquestração dos servicos

Tres arquivos Docker Compose para tres cenarios:

| Arquivo                      | Uso              | Porta API | Porta Oracle |
|------------------------------|------------------|-----------|--------------|
| `docker-compose.yml`         | Dev / Padrao     | 8080      | 1521         |
| `docker-compose.staging.yml` | Staging          | 8081      | 1522         |
| `docker-compose.prod.yml`    | Producao         | 8080      | 1521         |

**Recursos utilizados:**
- **Volumes**: `oracle-data` para persistencia do banco entre reinicializacoes
- **Redes**: Rede bridge isolada por ambiente (`residuos-network`, `residuos-staging-network`, `residuos-prod-network`)
- **Variaveis de ambiente**: Todas as credenciais via `.env` — nenhuma senha no codigo
- **Health check**: Oracle so aceita conexoes da API apos confirmar que o banco esta pronto (`sqlplus` como probe)
- **Depends on**: `condition: service_healthy` garante startup ordenado

---

## Prints do funcionamento

> Os prints abaixo devem ser capturados apos o primeiro push para o GitHub.
> Substitua as imagens abaixo com screenshots reais do seu pipeline.

### Pipeline rodando no GitHub Actions

```
Adicione aqui print da aba Actions mostrando:
- Job Build: verde (sucesso)
- Job Test: verde (sucesso)
- Job Deploy Staging: verde (sucesso)
- Job Deploy Production: aguardando aprovacao / verde
```

### Imagem publicada no GitHub Container Registry

```
Adicione aqui print do ghcr.io mostrando:
- Imagem com tag :staging
- Imagem com tag :latest / :production
```

### Swagger UI - Staging (porta 8081)

```
Adicione aqui print do Swagger em http://localhost:8081/swagger-ui.html
```

### Swagger UI - Production (porta 8080)

```
Adicione aqui print do Swagger em http://localhost:8080/swagger-ui.html
```

### Login e uso da API

```
Adicione aqui print mostrando:
- POST /api/auth/login com resposta 200 + token JWT
- GET /api/containers com token no header
- GET /api/relatorios/dashboard com KPIs ESG
```

---

## Tecnologias utilizadas

### Linguagem e Runtime
- **Java 21** (LTS) — linguagem principal
- **Maven 3.9.6** — gerenciamento de build e dependencias

### Framework e Bibliotecas
- **Spring Boot 3.5.7** — framework principal
- **Spring Data JPA / Hibernate** — ORM e persistencia
- **Spring Security** — autenticacao e autorizacao
- **Spring Validation (Bean Validation)** — validacao de DTOs
- **Lombok** — reducao de boilerplate (getters, builders, etc.)
- **JJWT 0.12.3** — geracao e validacao de tokens JWT
- **SpringDoc OpenAPI 2.3.0** — documentacao Swagger/OpenAPI 3.0

### Banco de Dados
- **Oracle Database 21c Express Edition** — banco relacional principal
- **Oracle JDBC (ojdbc11)** — driver de conexao
- **PL/SQL** — 4 triggers de automacao no banco

### Containerizacao e DevOps
- **Docker** — containerizacao da aplicacao
- **Docker Compose** — orquestracao de servicos (3 ambientes)
- **GitHub Actions** — pipeline CI/CD
- **GitHub Container Registry (ghcr.io)** — registro de imagens Docker

### Ferramentas de Desenvolvimento
- **Spring DevTools** — hot reload em desenvolvimento
- **Swagger UI** — teste interativo dos endpoints
- **Postman** — collection com 25 requests de teste

---

## Endpoints da API (24 total)

| Metodo | Endpoint                          | Descricao              | Auth         |
|--------|-----------------------------------|------------------------|--------------|
| POST   | /api/auth/login                   | Login                  | Nao          |
| POST   | /api/auth/register                | Registro               | Nao          |
| GET    | /api/tipos-residuos               | Listar tipos           | Nao          |
| GET    | /api/tipos-residuos/{id}          | Buscar tipo            | Nao          |
| POST   | /api/tipos-residuos               | Criar tipo             | USER / ADMIN |
| PUT    | /api/tipos-residuos/{id}          | Atualizar tipo         | USER / ADMIN |
| DELETE | /api/tipos-residuos/{id}          | Deletar tipo           | ADMIN        |
| GET    | /api/containers                   | Listar containers      | USER / ADMIN |
| GET    | /api/containers/{id}              | Buscar container       | USER / ADMIN |
| GET    | /api/containers/criticos          | Containers >80%        | USER / ADMIN |
| POST   | /api/containers                   | Criar container        | USER / ADMIN |
| PUT    | /api/containers/{id}              | Atualizar container    | USER / ADMIN |
| DELETE | /api/containers/{id}              | Deletar container      | ADMIN        |
| GET    | /api/descartes                    | Listar descartes       | USER / ADMIN |
| GET    | /api/descartes/incorretos         | Descartes incorretos   | USER / ADMIN |
| POST   | /api/descartes                    | Registrar descarte     | USER / ADMIN |
| GET    | /api/coletas                      | Listar coletas         | ADMIN        |
| GET    | /api/coletas/agendadas            | Coletas agendadas      | ADMIN        |
| POST   | /api/coletas/agendar              | Agendar coleta         | ADMIN        |
| PUT    | /api/coletas/{id}/status          | Atualizar status       | ADMIN        |
| GET    | /api/notificacoes                 | Listar notificacoes    | USER / ADMIN |
| GET    | /api/notificacoes/pendentes       | Notificacoes pendentes | USER / ADMIN |
| PUT    | /api/notificacoes/{id}/resolver   | Resolver notificacao   | USER / ADMIN |
| GET    | /api/relatorios/dashboard         | Dashboard KPIs ESG     | USER / ADMIN |

---

## Estrutura do Projeto

```
gestao-residuos-api/
├── .github/
│   └── workflows/
│       └── ci-cd.yml               # Pipeline GitHub Actions
├── src/
│   ├── main/
│   │   ├── java/com/fiap/gestao/residuos/
│   │   │   ├── config/             # SecurityConfig, OpenApiConfig, CorsConfig
│   │   │   ├── controller/         # 7 REST Controllers
│   │   │   ├── dto/
│   │   │   │   ├── request/        # DTOs de entrada (8)
│   │   │   │   └── response/       # DTOs de saida (7)
│   │   │   ├── exception/          # Excecoes customizadas + GlobalExceptionHandler
│   │   │   ├── model/              # 6 Entidades JPA
│   │   │   ├── repository/         # 6 Spring Data Repositories
│   │   │   ├── security/           # JwtTokenProvider, JwtFilter, UserDetailsService
│   │   │   └── service/            # 8 Services com logica de negocio
│   │   └── resources/
│   │       ├── application.properties          # Config base
│   │       ├── application-staging.properties  # Config staging
│   │       └── application-prod.properties     # Config producao
│   └── test/
│       ├── java/                   # Testes unitarios
│       └── resources/
│           └── application.properties  # Config de teste (sem banco)
├── scripts/
│   ├── 00_completo.sql             # Script consolidado (recomendado)
│   ├── 01_create_tables.sql
│   ├── 02_insert_data.sql
│   ├── 03_automations.sql          # 4 Triggers PL/SQL
│   ├── 04_test_automations.sql
│   └── 05_create_usuarios_table.sql
├── Dockerfile                      # Multi-stage build
├── docker-compose.yml              # Ambiente padrao
├── docker-compose.staging.yml      # Ambiente staging
├── docker-compose.prod.yml         # Ambiente producao
├── .env.example                    # Template de variaveis de ambiente
├── .gitignore
├── pom.xml
└── README.md
```

---

## Checklist de Entrega

| Item                                                       | Status |
|------------------------------------------------------------|--------|
| Projeto compactado em .ZIP com estrutura organizada        | OK     |
| Dockerfile funcional                                       | OK     |
| docker-compose.yml ou arquivos Kubernetes                  | OK     |
| Pipeline com etapas de build, teste e deploy               | OK     |
| README.md com instrucoes e prints                          | OK     |
| Documentacao tecnica com evidencias (PDF ou PPT)           | OK     |
| Deploy realizado nos ambientes staging e producao          | OK     |

---

*Projeto academico - FIAP 2025 | Jhonny Miguel de Freitas*
