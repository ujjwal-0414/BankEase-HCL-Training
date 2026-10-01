import {CommonModule} from '@angular/common';
import {Component, OnInit} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {ApiService} from '../../core/api.service';
import {Account, Investment, InvestmentProduct} from '../../core/models';
import {EmptyComponent, SpinnerComponent, StatusComponent} from '../../shared/ui';

@Component({
    selector: 'app-investments',
    standalone: true,
    imports: [CommonModule, FormsModule, StatusComponent, SpinnerComponent, EmptyComponent],
    template: `<div class="page-head"><div><span class="eyebrow dark">GROW YOUR MONEY</span><h1>Investments</h1><p>Explore BankEase investment products and track your holdings.</p></div></div>@if(error){<div class="alert error">{{error}}</div>}@if(success){<div class="alert success">{{success}}</div>}<section class="product-grid">@for(p of products;track p.id){<div class="product-card"><span class="product-type">{{p.type}}</span><h3>{{p.name}}</h3><strong>{{p.expectedAnnualReturn}}%</strong><span>Expected annual return</span><small>Minimum ₹{{p.minimumInvestment|number:'1.0-0'}}</small></div>}</section><div class="two-col"><section class="card form-card"><div class="card-head"><div><h2>Invest</h2><p>Choose an account and product.</p></div></div><form class="form-stack" (ngSubmit)="invest()"><label>Funding account<select name="account" [(ngModel)]="form.accountId" required><option value="">Select account</option>@for(a of accounts;track a.id){<option [value]="a.id">{{a.accountType}} · {{a.accountNumber}}</option>}</select></label><label>Product<select name="product" [(ngModel)]="form.productId" required><option value="">Select product</option>@for(p of products;track p.id){<option [value]="p.id">{{p.name}} · {{p.expectedAnnualReturn}}%</option>}</select></label><label>Amount<input name="amount" type="number" min="1" step="100" [(ngModel)]="form.amount" required></label><button class="primary full" [disabled]="busy">{{busy?'Processing...':'Invest now'}}</button></form></section><section class="card table-card"><div class="card-head"><div><h2>My investments</h2><p>Current holdings</p></div></div>@if(items.length){<div class="table-wrap"><table><thead><tr><th>Reference</th><th>Product</th><th>Principal</th><th>Current value</th><th>Status</th><th></th></tr></thead><tbody>@for(i of items;track i.id){<tr><td class="mono">{{i.referenceNumber}}</td><td>{{i.productName}}</td><td>₹{{i.principalAmount|number:'1.0-0'}}</td><td>₹{{i.currentValue|number:'1.0-0'}}</td><td><app-status [status]="i.status"/></td><td>@if(i.status==='ACTIVE'){<button class="text-button danger" (click)="redeem(i.id)">Redeem</button>}</td></tr>}</tbody></table></div>}@else{<app-empty title="No investments" text="Your investment holdings will appear here."/>}</section></div>`
})
export class InvestmentsComponent implements OnInit {
    accounts: Account[] = [];
    products: InvestmentProduct[] = [];
    items: Investment[] = [];
    busy = false;
    error = '';
    success = '';
    form: any = {accountId: '', productId: '', amount: ''};

    constructor(private api: ApiService) {
    }

    ngOnInit() {
        Promise.all([this.api.accounts.list().toPromise(), this.api.investments.products().toPromise(), this.api.investments.list().toPromise()]).then(([a, p, i]) => {
            this.accounts = a || [];
            this.products = p || [];
            this.items = i || []
        }).catch(e => this.error = e?.error?.message || 'Unable to load investments.')
    }

    invest() {
        this.busy = true;
        this.api.investments.invest({
            ...this.form,
            accountId: Number(this.form.accountId),
            productId: Number(this.form.productId),
            amount: Number(this.form.amount)
        }).subscribe({
            next: r => {
                this.success = `Investment ${r.referenceNumber} created.`;
                this.form = {...this.form, amount: ''};
                this.api.investments.list().subscribe(x => this.items = x)
            }, error: e => {
                this.error = e?.error?.message || 'Investment failed.';
                this.busy = false
            }, complete: () => this.busy = false
        })
    }

    redeem(id: number) {
        if (!confirm('Redeem this investment?')) return;
        this.api.investments.redeem(id).subscribe({
            next: r => {
                this.success = `Investment ${r.referenceNumber} redeemed.`;
                this.api.investments.list().subscribe(x => this.items = x)
            }, error: e => this.error = e?.error?.message || 'Redemption failed.'
        })
    }
}
