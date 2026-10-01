# BankEase — Full-Stack Digital Banking Application

**Owner:** Ujjwal Upadhyay  
**Backend:** Java 21, Spring Boot 4.1.0, Spring Security, JWT, JPA/Hibernate, MySQL  
**Frontend:** Angular 20, TypeScript, Angular Router, HttpClient, standalone components

## Project structure

```text
BankEase_Ujjwal_Upadhyay_FullStack/
├── backend/       # Existing tested Spring Boot + MySQL backend
└── frontend/      # Angular banking dashboard
```

## Run the backend

1. Create the MySQL database:

```sql
CREATE
DATABASE IF NOT EXISTS bankease
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

2. In `backend/src/main/resources/application.properties`, use your local MySQL credentials.
3. Start `BankEaseApplication.java`.
4. Backend URL: `http://localhost:8080`
5. API base: `http://localhost:8080/api`

The current backend uses Hibernate `ddl-auto=update`, so the JPA tables are created/updated when the backend starts.

## Run the Angular frontend

Requirements: Node.js 20+ recommended.

```bash
cd frontend
npm install
npm start
```

Open:

```text
http://localhost:4200
```

Angular's development server runs on port 4200, which is already allowed by the current BankEase backend CORS
configuration.

## Frontend features

- Login and registration
- JWT token storage and automatic Authorization header
- Protected routes and admin route guard
- Banking dashboard
- Account creation and account overview
- Beneficiary management
- Fund transfer workflow
- Bill payment workflow
- Loan application and tracking
- Investment products, investment and redemption
- Admin user/KYC/access management
- Admin loan decisions
- Responsive desktop/tablet/mobile layout
- Error and success feedback

## API alignment

The Angular application is wired to the **actual current backend routes** under `/api`, including:

- `/api/auth/register`
- `/api/auth/login`
- `/api/auth/health`
- `/api/accounts`
- `/api/beneficiaries`
- `/api/transfers`
- `/api/bill-payments`
- `/api/loans`
- `/api/investments`
- `/api/admin/users`
- `/api/admin/loans/pending`

The frontend intentionally follows the uploaded backend instead of the older `/api/v1` master specification.

## Important

`node_modules/` is intentionally not included in the ZIP. Run `npm install` inside `frontend` after extracting the
project.

This is an academic/portfolio banking simulator. It is not a production banking core.
