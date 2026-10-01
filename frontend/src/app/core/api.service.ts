import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Account, AdminUser, Beneficiary, BillPayment, Investment, InvestmentProduct, Loan, Transfer} from './models';
import {API_BASE} from './auth.service';

@Injectable({providedIn: 'root'})
export class ApiService {
    accounts = {
        list: () => this.http.get<Account[]>(`${API_BASE}/accounts`),
        get: (id: number) => this.http.get<Account>(`${API_BASE}/accounts/${id}`),
        create: (d: any) => this.http.post<Account>(`${API_BASE}/accounts`, d)
    };
    beneficiaries = {
        list: () => this.http.get<Beneficiary[]>(`${API_BASE}/beneficiaries`),
        create: (d: any) => this.http.post<Beneficiary>(`${API_BASE}/beneficiaries`, d),
        remove: (id: number) => this.http.delete(`${API_BASE}/beneficiaries/${id}`)
    };
    transfers = {
        list: () => this.http.get<Transfer[]>(`${API_BASE}/transfers`),
        create: (d: any) => this.http.post<Transfer>(`${API_BASE}/transfers`, d)
    };
    bills = {
        list: () => this.http.get<BillPayment[]>(`${API_BASE}/bill-payments`),
        pay: (d: any) => this.http.post<BillPayment>(`${API_BASE}/bill-payments`, d)
    };
    loans = {
        list: () => this.http.get<Loan[]>(`${API_BASE}/loans`),
        apply: (d: any) => this.http.post<Loan>(`${API_BASE}/loans`, d)
    };
    investments = {
        products: () => this.http.get<InvestmentProduct[]>(`${API_BASE}/investments/products`),
        list: () => this.http.get<Investment[]>(`${API_BASE}/investments`),
        invest: (d: any) => this.http.post<Investment>(`${API_BASE}/investments`, d),
        redeem: (id: number) => this.http.post<Investment>(`${API_BASE}/investments/${id}/redeem`, {})
    };
    admin = {
        users: () => this.http.get<AdminUser[]>(`${API_BASE}/admin/users`),
        kyc: (id: number, status: string) => this.http.patch(`${API_BASE}/admin/users/${id}/kyc`, null, {params: {status}}),
        enabled: (id: number, value: boolean) => this.http.patch(`${API_BASE}/admin/users/${id}/enabled`, null, {params: {value}}),
        pendingLoans: () => this.http.get<Loan[]>(`${API_BASE}/admin/loans/pending`),
        decision: (id: number, d: any) => this.http.patch<Loan>(`${API_BASE}/admin/loans/${id}/decision`, d)
    };

    constructor(private http: HttpClient) {
    }
}
