import {CommonModule} from '@angular/common';
import {Component, OnInit} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {ApiService} from '../../core/api.service';
import {Loan} from '../../core/models';
import {EmptyComponent, SpinnerComponent, StatusComponent} from '../../shared/ui';

@Component({
    selector: 'app-loans',
    standalone: true,
    imports: [CommonModule, FormsModule, StatusComponent, SpinnerComponent, EmptyComponent],
    template: `<div class="page-head"><div><span class="eyebrow dark">CREDIT SERVICES</span><h1>Loans</h1><p>Apply for a loan and track its approval lifecycle.</p></div></div>@if(error){<div class="alert error">{{error}}</div>}@if(success){<div class="alert success">{{success}}</div>}<div class="two-col"><section class="card form-card"><div class="card-head"><div><h2>Apply for a loan</h2><p>Requested amount starts at ₹10,000.</p></div></div><form class="form-stack" (ngSubmit)="apply()"><label>Loan type<select name="type" [(ngModel)]="form.loanType"><option>PERSONAL</option><option>HOME</option><option>EDUCATION</option><option>VEHICLE</option></select></label><label>Requested amount<input name="amount" type="number" min="10000" step="1000" [(ngModel)]="form.requestedAmount" required></label><label>Tenure (months)<input name="tenure" type="number" min="6" max="360" [(ngModel)]="form.tenureMonths" required></label><button class="primary full" [disabled]="busy">{{busy?'Submitting...':'Submit application'}}</button></form></section><section class="card table-card"><div class="card-head"><div><h2>My applications</h2><p>Loan lifecycle and decisions</p></div></div>@if(loading){<app-spinner/>}@else if(items.length){<div class="loan-list">@for(l of items;track l.id){<div class="loan-item"><div><span class="eyebrow dark">{{l.applicationNumber}}</span><h3>{{l.loanType}} loan</h3><p>₹{{l.requestedAmount|number:'1.0-0'}} · {{l.tenureMonths}} months</p></div><div class="loan-right"><app-status [status]="l.status"/> <b>{{l.emiAmount?'₹'+(l.emiAmount|number:'1.0-0')+'/mo':'Under review'}}</b></div></div>}</div>}@else{<app-empty title="No applications" text="Your loan applications will appear here."/>}</section></div>`
})
export class LoansComponent implements OnInit {
    items: Loan[] = [];
    loading = true;
    busy = false;
    error = '';
    success = '';
    form: any = {loanType: 'PERSONAL', requestedAmount: '', tenureMonths: 12};

    constructor(private api: ApiService) {
    }

    ngOnInit() {
        this.load()
    }

    load() {
        this.api.loans.list().subscribe({
            next: r => this.items = r,
            error: e => this.error = e?.error?.message || 'Unable to load loans.',
            complete: () => this.loading = false
        })
    }

    apply() {
        this.busy = true;
        this.api.loans.apply({
            ...this.form,
            requestedAmount: Number(this.form.requestedAmount),
            tenureMonths: Number(this.form.tenureMonths)
        }).subscribe({
            next: r => {
                this.success = `Application ${r.applicationNumber} submitted.`;
                this.form = {...this.form, requestedAmount: ''};
                this.load()
            }, error: e => {
                this.error = e?.error?.message || 'Loan application failed.';
                this.busy = false
            }, complete: () => this.busy = false
        })
    }
}
