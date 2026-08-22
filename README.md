# 🗳️ Votação API — Desafio Técnico Sicredi

API REST para gerenciamento de sessões de votação em assembleias de cooperativismo, desenvolvida como desafio técnico para o processo seletivo da **Sicredi**.

---

## 📋 Sumário

- [Propósito](#-propósito)
- [Tecnologias](#-tecnologias)
- [Arquitetura](#-arquitetura)
- [Pré-requisitos](#-pré-requisitos)
- [Como executar](#-como-executar)
- [Análise de qualidade com SonarQube](#-análise-de-qualidade-com-sonarqube)
- [Endpoints e cURLs](#-endpoints-e-curls)
- [Documentação Swagger](#-documentação-swagger)
- [Regras de negócio](#-regras-de-negócio)
- [Executando os testes](#-executando-os-testes)

---

## 🎯 Propósito

No cooperativismo, cada associado possui um voto e as decisões são tomadas em assembleias por votação.

Esta API oferece os recursos necessários para:

- **Cadastrar pautas** a serem votadas
- **Abrir sessões de votação** com duração configurável (padrão: 1 minuto)
- **Registrar votos** dos associados (Sim/Não), garantindo que cada associado vote apenas uma vez por pauta
- **Contabilizar e retornar o resultado** da votação de uma pauta

---

## 🛠️ Tecnologias

| Tecnologia | Versão | Finalidade |
|---|---|---|
| Java | 21 | Linguagem principal |
| Spring Boot | 4.1.0 | Framework base |
| Spring Data JPA | — | Persistência de dados |
| Spring Validation | — | Validação de entrada |
| PostgreSQL | 16 | Banco de dados relacional |
| Hibernate | — | ORM / DDL automático |
| MapStruct | 1.6.3 | Mapeamento entre camadas |
| Lombok | 1.18.30 | Redução de boilerplate |
| SpringDoc OpenAPI | 2.8.9 | Documentação Swagger |
| JUnit 5 + Mockito | — | Testes unitários e de integração |
| H2 (test) | — | Banco em memória para testes |
| JaCoCo | 0.8.13 | Cobertura de testes |
| SonarQube | Community | Análise estática e qualidade de código |
| Docker + Docker Compose | — | Infraestrutura local |
| Gradle | 9.x | Build e gerenciamento de dependências |

---

## 🏗️ Arquitetura

O projeto segue inspiração em **Arquitetura Clean e Onion**, organizada em camadas:

```text
src/main/java/.../votacao/
├── domain/                         # Entidades e regras de negócio puras
│   ├── Pauta.java
│   ├── SessaoVotacao.java
│   ├── Voto.java
│   ├── ResultadoVotacao.java
│   ├── TipoVoto.java               # Enum: SIM | NAO
│   └── exception/                  # Exceções de domínio
├── application/                    # Casos de uso (serviços)
│   ├── PautaService.java
│   ├── SessaoVotacaoService.java
│   ├── VotoService.java
│   └── repository/                 # Interfaces (portas de saída)
└── infraestructure/
    ├── config/                     # Configurações (Clock, OpenAPI)
    ├── exception/                  # GlobalExceptionHandler
    └── persistence/
        ├── entity/                 # Entidades JPA
        ├── mapper/                 # MapStruct mappers
        └── repository/
            ├── adapter/            # Implementações das portas
            └── Jpa*Repository.java # Spring Data JPA
    └── presentation/
        ├── controller/             # Controllers REST
        └── dto/
            ├── request/
            └── response/
```

---

## 📦 Pré-requisitos

- **Java 21+** instalado
- **Docker** e **Docker Compose** instalados
- **Git** instalado

> O projeto utiliza o **Gradle Wrapper** (`./gradlew`), portanto não é necessário instalar o Gradle manualmente.

---

## 🚀 Como executar

### 1. Clone o repositório

```bash
git clone git@github.com:lino-tech-works/desafio-sicredi.git
cd desafio-sicredi
```

### 2. Suba a infraestrutura com Docker

```bash
docker compose up -d
```

O Docker Compose iniciará:

- **PostgreSQL 16** na porta `5432`
- **SonarQube Community** na porta `9000`

O PostgreSQL é utilizado pela aplicação e também pelo SonarQube, em bancos separados.

Para a aplicação:

- **Database:** `votacao-db`
- **Usuário:** `postgres`
- **Senha:** `123`

Para o SonarQube:

- **Database:** `sonarqube`
- **Usuário:** `sonar`
- **Senha:** `sonar`

Após a inicialização, o SonarQube estará disponível em:

**http://localhost:9000**

### 3. Execute a aplicação

```bash
./gradlew bootRun
```

A API estará disponível em:

**http://localhost:8082**

---

### ▶️ Variáveis de ambiente da aplicação (opcional)

Caso queira apontar para um banco diferente, configure:

```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=votacao-db
export DB_USERNAME=postgres
export DB_PASSWORD=123
```

---

## 📊 Análise de qualidade com SonarQube

O projeto possui integração com **SonarQube** para análise estática do código, identificação de bugs, vulnerabilidades, code smells, duplicações e cobertura de testes.

O SonarQube é executado localmente através do Docker Compose.

### 1. Acessar o SonarQube

Com a infraestrutura iniciada:

```bash
docker compose up -d
```

acesse:

**http://localhost:9000**

Na primeira utilização, o login padrão é:

```text
Usuário: admin
Senha: admin
```

O SonarQube solicitará a alteração da senha no primeiro acesso.

> Se o ambiente já tiver sido inicializado anteriormente, utilize as credenciais definidas nessa instância.

---

### 2. Criar o projeto no SonarQube

Na interface do SonarQube:

1. Acesse **Projects**
2. Selecione **Create Project**
3. Escolha **Create a local project**
4. Informe:

```text
Project display name: Votacao
Project key: votacao
```

5. Em **Analysis Method**, selecione **Locally**.

---

### 3. Criar o token de análise

Crie um **User Token** no SonarQube através de:

**My Account → Security → Generate Token**

Sugestão de nome:

```text
votacao-local
```

O token deve ser criado pelo próprio avaliador na instância local do SonarQube.

> ⚠️ **Importante:** o token é uma credencial pessoal e **não deve ser commitado no Git**.

---

### 4. Configurar o token no ambiente

No Linux/macOS:

```bash
export SONAR_TOKEN="SEU_TOKEN"
```

Para verificar se a variável está configurada sem exibir o token:

```bash
printenv SONAR_TOKEN >/dev/null && echo "SONAR_TOKEN configurado"
```

No Windows PowerShell:

```powershell
$env:SONAR_TOKEN="SEU_TOKEN"
```

---

### 5. Executar a análise

Execute:

```bash
./gradlew clean test jacocoTestReport sonar
```

O comando irá:

1. limpar os artefatos anteriores;
2. executar os testes;
3. gerar o relatório de cobertura do JaCoCo;
4. executar a análise do SonarQube;
5. enviar os resultados para a instância local do SonarQube.

Após a execução, acesse:

**http://localhost:9000**

e selecione o projeto **Votacao**.

---

### 6. Cobertura de testes

O projeto utiliza **JaCoCo** para geração do relatório de cobertura.

Alguns componentes de infraestrutura não são considerados no cálculo da cobertura:

- entidades JPA;
- mappers;
- DTOs de request;
- DTOs de response;
- configurações;
- exceções;
- classe principal da aplicação.

Essas mesmas exclusões são configuradas no SonarQube para manter a métrica de cobertura consistente entre JaCoCo e SonarQube.

A cobertura mínima configurada no build é de **75%**.

> **Importante:** nenhum token ou credencial do SonarQube é armazenado no repositório. Cada ambiente deve utilizar seu próprio token através da variável `SONAR_TOKEN`.

---

## 📡 Endpoints e cURLs

> **Base URL:** `http://localhost:8082`  
> **Content-Type:** `application/json`  
> **Accept:** `application/vnd.votacao.v1+json`

---

### 1. 📋 Criar Pauta

**`POST /pautas`**

Cadastra uma nova pauta para votação.

```bash
curl -X POST http://localhost:8082/pautas \
  -H "Content-Type: application/json" \
  -H "Accept: application/vnd.votacao.v1+json" \
  -d '{
    "titulo": "Aprovação do orçamento anual 2026"
  }'
```

**Resposta `201 Created`:**

```json
{
  "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "titulo": "Aprovação do orçamento anual 2026"
}
```

---

### 2. 🔓 Abrir Sessão de Votação

**`POST /pautas/{pautaId}/sessao`**

Abre uma sessão de votação para uma pauta. O campo `duracaoEmMinutos` é **opcional** — se omitido, a sessão dura **1 minuto**.

**Com duração personalizada:**

```bash
curl -X POST http://localhost:8082/pautas/a1b2c3d4-e5f6-7890-abcd-ef1234567890/sessao \
  -H "Content-Type: application/json" \
  -H "Accept: application/vnd.votacao.v1+json" \
  -d '{
    "duracaoEmMinutos": 5
  }'
```

**Com duração padrão (1 minuto) — sem body:**

```bash
curl -X POST http://localhost:8082/pautas/a1b2c3d4-e5f6-7890-abcd-ef1234567890/sessao \
  -H "Accept: application/vnd.votacao.v1+json"
```

**Resposta `201 Created`:**

```json
{
  "id": "f1e2d3c4-b5a6-7890-fedc-ba9876543210",
  "pautaId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "inicio": "2026-08-21T17:00:00Z",
  "fim": "2026-08-21T17:05:00Z"
}
```

---

### 3. 🗳️ Registrar Voto

**`POST /pautas/{pautaId}/sessao/{sessaoVotacaoId}/votos`**

Registra o voto de um associado. Cada CPF pode votar **apenas uma vez** por pauta, e a sessão precisa estar **aberta**.

**Voto SIM:**

```bash
curl -X POST http://localhost:8082/pautas/a1b2c3d4-e5f6-7890-abcd-ef1234567890/sessao/f1e2d3c4-b5a6-7890-fedc-ba9876543210/votos \
  -H "Content-Type: application/json" \
  -H "Accept: application/vnd.votacao.v1+json" \
  -d '{
    "cpf": "12345678901",
    "tipo": "SIM"
  }'
```

**Voto NÃO:**

```bash
curl -X POST http://localhost:8082/pautas/a1b2c3d4-e5f6-7890-abcd-ef1234567890/sessao/f1e2d3c4-b5a6-7890-fedc-ba9876543210/votos \
  -H "Content-Type: application/json" \
  -H "Accept: application/vnd.votacao.v1+json" \
  -d '{
    "cpf": "98765432100",
    "tipo": "NAO"
  }'
```

**Resposta `201 Created`:**

```json
{
  "id": "11223344-5566-7788-99aa-bbccddeeff00",
  "pautaId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "sessaoVotacaoId": "f1e2d3c4-b5a6-7890-fedc-ba9876543210",
  "cpf": "***.456.789-**",
  "tipo": "SIM",
  "criadoEm": "2026-08-21T17:02:30Z"
}
```

---

### 4. 📊 Consultar Resultado da Votação

**`GET /pautas/{pautaId}/resultado`**

Retorna a contagem de votos SIM e NÃO de uma pauta.

```bash
curl -X GET http://localhost:8082/pautas/a1b2c3d4-e5f6-7890-abcd-ef1234567890/resultado \
  -H "Accept: application/vnd.votacao.v1+json"
```

**Resposta `200 OK`:**

```json
{
  "pautaId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "quantidadeSim": 10,
  "quantidadeNao": 4
}
```

---

### ⚠️ Respostas de erro

| Código | Situação |
|---|---|
| `400` | Dados de entrada inválidos, sessão fechada, voto duplicado ou duração inválida |
| `404` | Pauta ou sessão não encontrada |
| `500` | Erro interno inesperado |

**Exemplo de erro `400` (sessão fechada):**

```json
{
  "type": "about:blank",
  "title": "Operação não permitida",
  "status": 400,
  "detail": "A sessão de votação está fechada.",
  "path": "/pautas/.../sessao/.../votos"
}
```

---

## 📖 Documentação Swagger

Com a aplicação em execução, acesse a documentação interativa:

- **Swagger UI:** http://localhost:8082/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8082/v3/api-docs

---

## 📐 Regras de negócio

- Uma **pauta** deve ter um título não nulo
- Uma **sessão** está vinculada a exatamente uma pauta
- A **duração padrão** de uma sessão é de **1 minuto**; pode ser configurada em minutos na abertura
- Votos aceitos: **`SIM`** ou **`NAO`**
- Um associado, identificado pelo **CPF**, pode votar **apenas uma vez por pauta**
- Não é possível votar em uma sessão **encerrada**
- Os dados são **persistidos em PostgreSQL** e sobrevivem a restarts da aplicação

---

## 🧪 Executando os testes

Para executar todos os testes:

```bash
./gradlew test
```

O projeto possui:

- **Testes unitários** de domínio e serviços, utilizando Mockito
- **Testes de integração** dos repositórios, utilizando H2 em memória
- **Testes de controller**, utilizando MockMvc e `@WebMvcTest`

### Cobertura com JaCoCo

Para executar os testes e gerar o relatório de cobertura:

```bash
./gradlew test jacocoTestReport
```

Os relatórios são gerados em:

```text
build/reports/jacoco/test/
```

O relatório HTML pode ser acessado em:

```text
build/reports/jacoco/test/html/index.html
```

A cobertura mínima configurada no build é de **75%**.

---

## 👤 Autor

**Lino Tech Works - GusttaDev**  
GitHub: https://github.com/lino-tech-works
