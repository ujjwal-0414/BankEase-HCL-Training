export interface UserSession {
    userId: number;
    name: string;
    email: string;
    role: 'USER' | 'ADMIN';
    token: string;
}

export interface Account {
    id: number;
    accountNumber: string;
    ifscCode: string;
    accountType: 'SAVINGS' | 'CURRENT';
    status: string;
    balance: number;
    dailyTransferLimit: number;
}

export interface Beneficiary {
    id: number;
    name: string;
    accountNumber: string;
    ifscCode: string;
    bankName?: string;
    nickname?: string;
    active: boolean;
}

export interface Transfer {
    id: number;
    referenceNumber: string;
    sourceAccountId: number;
    destinationAccountNumber: string;
    destinationIfsc: string;
    mode: string;
    amount: number;
    status: string;
    remarks?: string;
    createdAt: string;
}

export interface BillPayment {
    id: number;
    referenceNumber: string;
    accountId: number;
    billerName: string;
    billerCategory: string;
    consumerNumber: string;
    amount: number;
    status: string;
    createdAt: string;
}

export interface Loan {
    id: number;
    applicationNumber: string;
    loanType: string;
    requestedAmount: number;
    tenureMonths: number;
    interestRate?: number;
    emiAmount?: number;
    status: string;
    decisionRemarks?: string;
    createdAt: string;
    decidedAt?: string;
}

export interface InvestmentProduct {
    id: number;
    code: string;
    name: string;
    type: string;
    expectedAnnualReturn: number;
    minimumInvestment: number;
    active: boolean;
}

export interface Investment {
    id: number;
    referenceNumber: string;
    productId: number;
    productName: string;
    principalAmount: number;
    currentValue: number;
    status: string;
    purchasedAt: string;
    redeemedAt?: string;
}

export interface AdminUser {
    id: number;
    name: string;
    email: string;
    role: string;
    kycStatus: string;
    enabled: boolean;
    failedLoginAttempts: number;
}

export interface ApiError {
    message?: string;
    error?: string;
}
