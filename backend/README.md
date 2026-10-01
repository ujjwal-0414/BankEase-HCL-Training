# BankEase — NextGen Net Banking Backend

BankEase is a Spring Boot REST backend for a net-banking application covering customer registration/login, account
management, beneficiaries, fund transfers, bill payments, loans and investments. The backend is intentionally designed
as a portfolio/academic banking simulator: no real bank, payment gateway or external clearing network is contacted.

## Stack

- Java 21
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Data JPA / Hibernate
- Spring Security
- JWT (JJWT 0.12.6)
- BCrypt password hashing
- MySQL
- Lombok
- Bean Validation

## Architecture

`Angular -> REST Controller -> Service -> Repository -> MySQL`

Security is handled before the controller through `SecurityConfig` + `JwtAuthenticationFilter`. Business rules stay in
service implementations. DTOs keep entities out of API contracts.

## Main modules

1. Authentication + JWT + BCrypt
2. User / role / KYC controls
3. Bank accounts
4. Beneficiary management
5. Fund transfer with account ownership checks, daily limits and pessimistic locking
6. Bill payment
7. Loan application + admin decision + EMI calculation
8. Investment product catalog + investment/redeem flow
9. Audit logging
10. Global 401/403 and exception responses

## CORS — follows the ShopSphere convention

The previous ShopSphere project used global CORS in `SecurityConfig` rather than controller-level `@CrossOrigin`.
BankEase follows the same pattern, with the Angular development origin changed to:

`http://localhost:4200`

Allowed methods: GET, POST, PUT, DELETE, PATCH, OPTIONS.
Allowed headers: Authorization, Content-Type.
Credentials: enabled.

There is no Vite proxy because the frontend will be Angular. Angular will call `http://localhost:8080` directly.

## Database setup

Install MySQL Server 8.x and MySQL Workbench. Then run:

```sql
CREATE DATABASE bankease CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Set environment variables:

```text
DB_URL=jdbc:mysql://localhost:3306/bankease?useSSL=false&serverTimezone=Asia/Kolkata&allowPublicKeyRetrieval=true
DB_USERNAME=root
DB_PASSWORD=YOUR_MYSQL_PASSWORD
JWT_SECRET=YOUR_BASE64_SECRET
JWT_EXPIRATION=86400000
```

`spring.jpa.hibernate.ddl-auto=update` creates/updates the tables automatically on first run. `DataInitializer` inserts
USER/ADMIN roles, three investment products and a demo admin account.

### Demo admin

- Email: `admin@bankease.com`
- Password: `Admin@12345`

Change this before using the project anywhere beyond local development.

## API overview

### Public

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/auth/health`
- `GET /api/investments/products`

### Authenticated customer

- `POST /api/accounts`
- `GET /api/accounts`
- `GET /api/accounts/{id}`
- `POST /api/beneficiaries`
- `GET /api/beneficiaries`
- `DELETE /api/beneficiaries/{id}`
- `POST /api/transfers`
- `GET /api/transfers`
- `POST /api/bill-payments`
- `GET /api/bill-payments`
- `POST /api/loans`
- `GET /api/loans`
- `POST /api/investments`
- `GET /api/investments`
- `POST /api/investments/{id}/redeem`

### Admin

- `GET /api/admin/users`
- `PATCH /api/admin/users/{id}/kyc?status=VERIFIED`
- `PATCH /api/admin/users/{id}/enabled?value=false`
- `GET /api/admin/loans/pending`
- `PATCH /api/admin/loans/{id}/decision`

## Important banking rules implemented

- BCrypt hashes passwords; raw passwords are never stored.
- JWT authentication is stateless.
- USER and ADMIN are enforced with RBAC.
- A customer can only operate their own accounts, beneficiaries and loans/investments.
- KYC must be VERIFIED before fund transfers.
- Source accounts are locked pessimistically during balance-changing operations.
- Transfer amount is checked against balance and daily transfer limit.
- IMPS is capped at ₹5,00,000 per simulated transaction.
- RTGS requires at least ₹2,00,000 in the simulator.
- Bill payments and investments cannot overdraw an account.
- Loan approval requires an interest rate and calculates EMI.
- Audit logs record security-sensitive business actions.
- Custom authentication entry point returns 401; custom access denied handler returns 403.

## Running

From IntelliJ: open the project as a Maven project, configure environment variables in the Run Configuration, and run
`BankEaseApplication`.

Or, with Maven installed:

```bash
mvn clean spring-boot:run
```

The API starts at `http://localhost:8080`.

## Angular integration

The future Angular frontend should use:

`http://localhost:8080/api/...`

Attach the JWT to protected calls:

`Authorization: Bearer <token>`

The Angular dev server should run at `http://localhost:4200`, matching the CORS rule in `SecurityConfig`.

## Production note

This is a complete academic/portfolio backend simulator, not a production banking core. A real deployment would
additionally require MFA/OTP, HSM-backed secrets, encryption/key rotation, maker-checker workflows, KYC/AML
integrations, fraud detection, rate limiting, idempotency keys, transaction reconciliation, external payment/clearing
integrations, monitoring, backups, and formal compliance controls.
