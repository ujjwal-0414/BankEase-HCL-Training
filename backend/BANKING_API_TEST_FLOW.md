# BankEase Postman Test Flow

1. Register customer.
2. Login customer and copy `token`.
3. Create account with initial deposit, e.g. 100000.
4. Login admin using the demo admin credentials.
5. Admin verifies customer KYC: `PATCH /api/admin/users/{id}/kyc?status=VERIFIED`.
6. Customer adds a beneficiary using the customer token.
7. Customer creates a second account for another test user OR use another registered customer as an internal beneficiary
   destination.
8. Customer transfers money.
9. Customer pays a bill.
10. Customer applies for a loan; admin approves/rejects it.
11. Customer purchases an investment and later redeems it.
12. Verify balances and audit-sensitive responses after each operation.

For protected endpoints add:
`Authorization: Bearer <customer-or-admin-jwt>`

Never send the password as a query parameter.
