# Authentication Service

Microsserviço de autenticação e gerenciamento de usuários do sistema **Jira 2.0 Help Desk**. Responsável por registro, login com JWT, recuperação de senha e administração de contas.

> **Porta padrão:** `8080`

---

## Tecnologias

- Java 17 + Spring Boot 3
- Spring Security (filtro JWT customizado)
- Spring Data JPA + Hibernate
- BCrypt para hash de senhas
- JavaMailSender para e-mails de recuperação
- Lombok

---

## Estrutura do projeto

```
src/main/java/com/example/authentication/
├── config/
│   └── CorsConfig.java
├── controller/
│   ├── AuthController.java        # /auth/**
│   └── AdminController.java       # /admin/**
├── dto/
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   ├── RegisterRequest.java
│   ├── ResetRequest.java
│   ├── CompleteResetRequest.java
│   └── admin/
│       ├── UserSummaryResponse.java
│       ├── UserDetailResponse.java
│       ├── UpdateRoleRequest.java
│       └── AuditSummary.java
├── enums/
│   ├── Type.java
│   ├── FailureReason.java
│   └── PasswordChangeReason.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── UnauthorizedException.java
│   ├── UserAlreadyExistsException.java
│   └── InvalidResetRequestException.java
├── model/
│   ├── User.java
│   ├── LoginAudit.java
│   ├── PasswordHistory.java
│   └── PasswordReset.java
├── repository/
│   ├── UserRepository.java
│   ├── LoginAuditRepository.java
│   ├── PasswordHistoryRepository.java
│   └── PasswordResetRepository.java
├── security/
│   ├── SecurityConfig.java
│   └── JwtAuthenticationFilter.java
└── service/
    ├── AuthService.java
    ├── AdminService.java
    ├── JwtService.java
    ├── UserService.java
    ├── EmailService.java
    └── PasswordResetService.java
```

---

## Configuração

Crie um arquivo `application-dev.properties` (não suba para o Git) com as seguintes variáveis ou exporte-as como variáveis de ambiente:

```properties
# Banco de dados
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASS}

# JWT — chave secreta em Base64
app.jwt.secret=${JWT_SECRET}
app.jwt.expiration=86400000

# CORS
cors.allowed-origins=http://localhost:4200

```

> O `JWT_SECRET` deve ser a mesma chave usada no **Jira Service**. Ambos compartilham o segredo para validação do token.

---

## Endpoints

### Públicos — `/auth`

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/auth/register` | Cadastra novo usuário |
| `POST` | `/auth/login` | Autentica e retorna JWT |
| `POST` | `/auth/forgot-password` | Envia e-mail de recuperação de senha |
| `POST` | `/auth/reset-password` | Conclui a troca de senha via token |

#### `POST /auth/register`
```json
{
  "name": "João Silva",
  "birthDate": "1998-05-20",
  "username": "joaosilva",
  "phoneNumber": "84999999999",
  "email": "joao@email.com",
  "password": "senhaSegura123"
}
```

#### `POST /auth/login`
```json
{
  "login": "joaosilva",
  "password": "senhaSegura123"
}
```
Resposta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

O payload do JWT contém: `sub` (username), `userId`, `email`, `role`, `iat`, `exp`.

---

### Protegidos — `/admin` *(requer `ROLE_ADMIN` ou `ROLE_SUPERADMIN`)*

| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/admin/users` | Lista todos os usuários |
| `GET` | `/admin/users/{id}` | Detalha um usuário (com auditoria de acesso) |
| `PUT` | `/admin/users/{id}/role` | Altera o nível de acesso |
| `PUT` | `/admin/users/{id}/active?active=true` | Ativa ou desativa uma conta |

---

## Roles disponíveis

| Role | Descrição |
|------|-----------|
| `ROLE_VISITOR` | Acesso de visualização mínima (em teste) |
| `ROLE_NORMAL` | Usuário padrão — abre e acompanha chamados |
| `ROLE_ADMIN` | Gerencia usuários, times e chamados |
| `ROLE_SUPERADMIN` | Controle total, incluindo alterar roles |

---

## Funcionalidades de segurança

- Senhas hasheadas com **BCrypt**
- Tokens JWT com expiração configurável (padrão: 24h)
- Registro de tentativas de login com IP e User-Agent (`LoginAudit`)
- Histórico de senhas para evitar reutilização (`PasswordHistory`)
- Proteção contra timing attacks na comparação de credenciais
- Recuperação de senha via token único com expiração

---

## Como executar

```bash
# Clone o repositório
git clone https://github.com/ranixx1/authentication.git
cd authentication

# Configure as variáveis de ambiente (ou crie application-dev.properties)
export DB_URL=jdbc:mysql://localhost:3306/auth_db
export DB_USER=root
export DB_PASS=sua_senha
export JWT_SECRET=suaChaveBase64AquiComPeloMenos256Bits

# Execute
./mvnw spring-boot:run
```

O serviço sobe em `http://localhost:8080`.

---

## Relacionamento com outros serviços

```
[Frontend Angular :4200]
        │
        ▼ POST /auth/login → recebe JWT
[Auth Service :8080]
        │
        │ JWT compartilhado (mesmo secret)
        ▼
[Jira Service :8082] — valida o token em cada requisição
```

---

## Roadmap planejado

- [ ] Endpoint de perfil do usuário (`GET /users/me`)
- [ ] Refresh token
- [ ] 2FA via e-mail ou TOTP