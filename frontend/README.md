# BankEase Angular Frontend

Angular 20 standalone frontend for the existing BankEase Spring Boot backend.

## Start

```bash
npm install
npm start
```

Open `http://localhost:4200`.

The backend must be running at `http://localhost:8080`.

## Main screens

Login, Register, Dashboard, Accounts, Beneficiaries, Transfers, Bill Payments, Loans, Investments and Admin Center.

The app stores the JWT returned by the backend in browser local storage and adds it as a Bearer token to protected API
requests.
