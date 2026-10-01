import {CommonModule} from '@angular/common';
import {Component, OnInit} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {ApiService} from '../../core/api.service';
import {Account} from '../../core/models';
import {EmptyComponent, SpinnerComponent, StatusComponent} from '../../shared/ui';

@Component({
    selector: 'app-accounts',
    standalone: true,
    imports: [CommonModule, FormsModule, SpinnerComponent, StatusComponent, EmptyComponent],
    template: `<div class="page-head"><div><span class="eyebrow dark">YOUR MONEY</span><h1>Accounts</h1><p>View balances and create additional savings or current accounts.</p></div><button class="primary" (click)="show=true">+ Open account</button></div>@if(error){<div class="alert error">{{error}}</div>}@if(loading){<app-spinner/>}@else{<div class="account-cards">@for(a of accounts;track a.id){<div class="bank-card"><div class="bank-card-top"><span>BankEase</span><span>{{a.accountType}}</span></div><div class="chip">◈</div><div class="bank-number">{{a.accountNumber}}</div><div class="bank-bottom"><div><small>Available balance</small><strong>₹{{a.balance|number:'1.2-2'}}</strong></div><div><small>IFSC</small><b>{{a.ifscCode}}</b></div></div></div>}@empty{<app-empty title="No accounts" text="Open your first BankEase account."/>}</div><section class="card table-card"><div class="card-head"><div><h2>Account details</h2><p>Ownership and status</p></div></div><div class="table-wrap"><table><thead><tr><th>Account</th><th>Type</th><th>Balance</th><th>Daily limit</th><th>Status</th></tr></thead><tbody>@for(a of accounts;track a.id){<tr><td class="mono">{{a.accountNumber}}</td><td>{{a.accountType}}</td><td>₹{{a.balance|number:'1.2-2'}}</td><td>₹{{a.dailyTransferLimit|number:'1.0-0'}}</td><td><app-status [status]="a.status"/></td></tr>}</tbody></table></div></section>}@if(show){<div class="modal-backdrop"><div class="modal"><div class="modal-head"><div><h2>Open an account</h2><p>Choose an account type and optional opening deposit.</p></div><button class="icon-btn" (click)="show=false">×</button></div><form (ngSubmit)="create()" class="form-stack"><label>Account type<select name="type" [(ngModel)]="form.accountType"><option value="SAVINGS">Savings</option><option value="CURRENT">Current</option></select></label><label>Initial deposit<input type="number" name="deposit" min="0" step="100" [(ngModel)]="form.initialDeposit"></label><button class="primary full" [disabled]="busy">{{busy?'Creating...':'Create account'}}</button></form></div></div>}`
})
export class AccountsComponent implements OnInit {
    accounts: Account[] = [];
    loading = true;
    show = false;
    busy = false;
    error = '';
    form: any = {accountType: 'SAVINGS', initialDeposit: 0};

    constructor(private api: ApiService) {
    }

    ngOnInit() {
        this.load()
    }

    load() {
        this.api.accounts.list().subscribe({
            next: r => this.accounts = r,
            error: e => this.error = e?.error?.message || 'Unable to load accounts.',
            complete: () => this.loading = false
        })
    }

    create() {
        this.busy = true;
        this.api.accounts.create({
            ...this.form,
            initialDeposit: Number(this.form.initialDeposit || 0)
        }).subscribe({
            next: () => {
                this.show = false;
                this.load()
            }, error: e => {
                this.error = e?.error?.message || 'Account creation failed.';
                this.busy = false
            }, complete: () => this.busy = false
        })
    }
}
