import {CommonModule} from '@angular/common';
import {Component, OnInit} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {ApiService} from '../../core/api.service';
import {AdminUser, Loan} from '../../core/models';
import {EmptyComponent, SpinnerComponent, StatusComponent} from '../../shared/ui';

@Component({
    selector: 'app-admin',
    standalone: true,
    imports: [CommonModule, FormsModule, StatusComponent, SpinnerComponent, EmptyComponent],
    template: `<div class="page-head"><div><span class="eyebrow dark">ADMINISTRATION</span><h1>Admin center</h1><p>Manage customers, KYC status and pending loan decisions.</p></div></div>@if(error){<div class="alert error">{{error}}</div>}@if(success){<div class="alert success">{{success}}</div>}<div class="stats-grid"><div class="stat-card"><div class="stat-top"><span>Total users</span><span class="stat-icon">◉</span></div><strong>{{users.length}}</strong><small>Customer records</small></div><div class="stat-card"><div class="stat-top"><span>Pending KYC</span><span class="stat-icon">!</span></div><strong>{{pendingKyc}}</strong><small>Need review</small></div><div class="stat-card"><div class="stat-top"><span>Pending loans</span><span class="stat-icon">◫</span></div><strong>{{loans.length}}</strong><small>Awaiting decision</small></div><div class="stat-card"><div class="stat-top"><span>Enabled users</span><span class="stat-icon success">✓</span></div><strong>{{enabledUsers}}</strong><small>Active access</small></div></div><section class="card table-card"><div class="card-head"><div><h2>User management</h2><p>KYC and account access controls</p></div></div>@if(loading){<app-spinner/>}@else if(users.length){<div class="table-wrap"><table><thead><tr><th>User</th><th>Role</th><th>KYC</th><th>Access</th><th>Actions</th></tr></thead><tbody>@for(u of users;track u.id){<tr><td><b>{{u.name}}</b><small class="cell-sub">{{u.email}}</small></td><td>{{u.role}}</td><td><app-status [status]="u.kycStatus"/></td><td><app-status [status]="u.enabled?'ENABLED':'DISABLED'"/></td><td class="actions"><button class="text-button" (click)="kyc(u.id,'VERIFIED')">Verify KYC</button><button class="text-button danger" (click)="enabled(u.id,!u.enabled)">{{u.enabled?'Disable':'Enable'}}</button></td></tr>}</tbody></table></div>}@else{<app-empty title="No users"/>}</section><section class="card table-card"><div class="card-head"><div><h2>Pending loan decisions</h2><p>Review applications</p></div></div>@if(loans.length){<div class="table-wrap"><table><thead><tr><th>Application</th><th>Type</th><th>Amount</th><th>Tenure</th><th>Decision</th></tr></thead><tbody>@for(l of loans;track l.id){<tr><td class="mono">{{l.applicationNumber}}</td><td>{{l.loanType}}</td><td>₹{{l.requestedAmount|number:'1.0-0'}}</td><td>{{l.tenureMonths}} mo</td><td><button class="text-button" (click)="decide(l.id,'APPROVED')">Approve</button><button class="text-button danger" (click)="decide(l.id,'REJECTED')">Reject</button></td></tr>}</tbody></table></div>}@else{<app-empty title="No pending loans" text="There are no applications waiting for review."/>}</section>`
})
export class AdminComponent implements OnInit {
    users: AdminUser[] = [];
    loans: Loan[] = [];
    loading = true;
    error = '';
    success = '';

    constructor(private api: ApiService) {
    }

    get pendingKyc() {
        return this.users.filter(u => u.kycStatus === 'PENDING').length
    }

    get enabledUsers() {
        return this.users.filter(u => u.enabled).length
    }

    ngOnInit() {
        this.load()
    }

    load() {
        Promise.all([this.api.admin.users().toPromise(), this.api.admin.pendingLoans().toPromise()]).then(([u, l]) => {
            this.users = u || [];
            this.loans = l || []
        }).catch(e => this.error = e?.error?.message || 'Unable to load admin data.').finally(() => this.loading = false)
    }

    kyc(id: number, status: string) {
        this.api.admin.kyc(id, status).subscribe({
            next: () => {
                this.success = 'KYC status updated.';
                this.load()
            }, error: e => this.error = e?.error?.message || 'KYC update failed.'
        })
    }

    enabled(id: number, value: boolean) {
        this.api.admin.enabled(id, value).subscribe({
            next: () => {
                this.success = 'User access updated.';
                this.load()
            }, error: e => this.error = e?.error?.message || 'Access update failed.'
        })
    }

    decide(id: number, status: string) {
        this.api.admin.decision(id, {
            status,
            interestRate: status === 'APPROVED' ? 10 : null,
            decisionRemarks: `Decision by BankEase admin`
        }).subscribe({
            next: () => {
                this.success = `Loan ${status.toLowerCase()}.`;
                this.load()
            }, error: e => this.error = e?.error?.message || 'Loan decision failed.'
        })
    }
}
