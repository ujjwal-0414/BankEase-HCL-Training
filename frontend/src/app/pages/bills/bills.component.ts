import {CommonModule} from '@angular/common';
import {Component, OnInit} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {ApiService} from '../../core/api.service';
import {Account, BillPayment} from '../../core/models';
import {EmptyComponent, SpinnerComponent, StatusComponent} from '../../shared/ui';

@Component({
    selector: 'app-bills',
    standalone: true,
    imports: [CommonModule, FormsModule, SpinnerComponent, StatusComponent, EmptyComponent],
    template: `<div class="page-head"><div><span class="eyebrow dark">PAY & TRACK</span><h1>Bill payments</h1><p>Pay utilities directly from a BankEase account.</p></div></div>@if(error){<div class="alert error">{{error}}</div>}@if(success){<div class="alert success">{{success}}</div>}<div class="two-col"><section class="card form-card"><div class="card-head"><div><h2>Pay a bill</h2><p>Use an active account with sufficient balance.</p></div></div><form class="form-stack" (ngSubmit)="pay()"><label>Account<select name="account" [(ngModel)]="form.accountId" required><option value="">Select account</option>@for(a of accounts;track a.id){<option [value]="a.id">{{a.accountType}} · {{a.accountNumber}}</option>}</select></label><label>Biller name<input name="biller" [(ngModel)]="form.billerName" required placeholder="Electricity provider"></label><label>Category<select name="category" [(ngModel)]="form.billerCategory"><option>Electricity</option><option>Water</option><option>Internet</option><option>Mobile</option><option>Gas</option></select></label><label>Consumer number<input name="consumer" [(ngModel)]="form.consumerNumber" required></label><label>Amount<input name="amount" type="number" min="1" step="0.01" [(ngModel)]="form.amount" required></label><button class="primary full" [disabled]="busy">{{busy?'Paying...':'Pay bill'}}</button></form></section><section class="card table-card"><div class="card-head"><div><h2>Payment history</h2><p>Your recent bill payments</p></div></div>@if(items.length){<div class="table-wrap"><table><thead><tr><th>Reference</th><th>Biller</th><th>Category</th><th>Amount</th><th>Status</th></tr></thead><tbody>@for(b of items;track b.id){<tr><td class="mono">{{b.referenceNumber}}</td><td>{{b.billerName}}</td><td>{{b.billerCategory}}</td><td>₹{{b.amount|number:'1.2-2'}}</td><td><app-status [status]="b.status"/></td></tr>}</tbody></table></div>}@else{<app-empty title="No payments yet" text="Your bill payment history will appear here."/>}</section></div>`
})
export class BillsComponent implements OnInit {
    accounts: Account[] = [];
    items: BillPayment[] = [];
    busy = false;
    error = '';
    success = '';
    form: any = {accountId: '', billerName: '', billerCategory: 'Electricity', consumerNumber: '', amount: ''};

    constructor(private api: ApiService) {
    }

    ngOnInit() {
        Promise.all([this.api.accounts.list().toPromise(), this.api.bills.list().toPromise()]).then(([a, b]) => {
            this.accounts = a || [];
            this.items = b || []
        }).catch(e => this.error = e?.error?.message || 'Unable to load bill payments.')
    }

    pay() {
        this.busy = true;
        this.api.bills.pay({
            ...this.form,
            accountId: Number(this.form.accountId),
            amount: Number(this.form.amount)
        }).subscribe({
            next: r => {
                this.success = `Bill payment ${r.referenceNumber} submitted.`;
                this.form = {...this.form, amount: '', consumerNumber: ''};
                this.api.bills.list().subscribe(x => this.items = x)
            }, error: e => {
                this.error = e?.error?.message || 'Bill payment failed.';
                this.busy = false
            }, complete: () => this.busy = false
        })
    }
}
