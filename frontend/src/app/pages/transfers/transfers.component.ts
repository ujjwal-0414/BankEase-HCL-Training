import {CommonModule} from '@angular/common';
import {Component, OnInit} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {ApiService} from '../../core/api.service';
import {Account, Beneficiary, Transfer} from '../../core/models';
import {EmptyComponent, SpinnerComponent, StatusComponent} from '../../shared/ui';

@Component({
    selector: 'app-transfers',
    standalone: true,
    imports: [CommonModule, FormsModule, SpinnerComponent, StatusComponent, EmptyComponent],
    template: `<div class="page-head"><div><span class="eyebrow dark">MOVE MONEY</span><h1>Transfers</h1><p>Send money from your BankEase account to a saved beneficiary.</p></div></div>@if(error){<div class="alert error">{{error}}</div>}@if(success){<div class="alert success">{{success}}</div>}<div class="two-col"><section class="card form-card"><div class="card-head"><div><h2>New transfer</h2><p>Choose a source account and beneficiary.</p></div></div>@if(loading){<app-spinner/>}@else{<form class="form-stack" (ngSubmit)="send()"><label>Source account<select name="account" [(ngModel)]="form.sourceAccountId" required><option value="">Select account</option>@for(a of accounts;track a.id){<option [value]="a.id">{{a.accountType}} · {{a.accountNumber}} · ₹{{a.balance|number:'1.0-0'}}</option>}</select></label><label>Beneficiary<select name="beneficiary" [(ngModel)]="form.beneficiaryId" required><option value="">Select beneficiary</option>@for(b of beneficiaries;track b.id){<option [value]="b.id">{{b.nickname||b.name}} · {{b.accountNumber}}</option>}</select></label><label>Amount<input name="amount" type="number" min="0.01" step="0.01" [(ngModel)]="form.amount" required placeholder="₹0.00"></label><label>Transfer mode<select name="mode" [(ngModel)]="form.mode"><option>IMPS</option><option>NEFT</option><option>RTGS</option></select></label><label>Remarks<input name="remarks" maxlength="255" [(ngModel)]="form.remarks" placeholder="Optional note"></label><button class="primary full" [disabled]="busy">{{busy?'Processing...':'Review & send'}}</button></form>}</section><section class="card table-card"><div class="card-head"><div><h2>Transfer history</h2><p>Latest money movement</p></div></div>@if(transfers.length){<div class="table-wrap"><table><thead><tr><th>Reference</th><th>Destination</th><th>Mode</th><th>Amount</th><th>Status</th></tr></thead><tbody>@for(t of transfers;track t.id){<tr><td class="mono">{{t.referenceNumber}}</td><td>{{t.destinationAccountNumber}}</td><td>{{t.mode}}</td><td class="amount">₹{{t.amount|number:'1.2-2'}}</td><td><app-status [status]="t.status"/></td></tr>}</tbody></table></div>}@else{<app-empty title="No transfers yet" text="Completed and pending transfers will appear here."/>}</section></div>`
})
export class TransfersComponent implements OnInit {
    accounts: Account[] = [];
    beneficiaries: Beneficiary[] = [];
    transfers: Transfer[] = [];
    loading = true;
    busy = false;
    error = '';
    success = '';
    form: any = {sourceAccountId: '', beneficiaryId: '', amount: '', mode: 'IMPS', remarks: ''};

    constructor(private api: ApiService) {
    }

    ngOnInit() {
        this.load()
    }

    load() {
        Promise.all([this.api.accounts.list().toPromise(), this.api.beneficiaries.list().toPromise(), this.api.transfers.list().toPromise()]).then(([a, b, t]) => {
            this.accounts = a || [];
            this.beneficiaries = b || [];
            this.transfers = t || []
        }).catch(e => this.error = e?.error?.message || 'Unable to load transfer data.').finally(() => this.loading = false)
    }

    send() {
        this.busy = true;
        this.error = '';
        this.success = '';
        const d = {
            sourceAccountId: Number(this.form.sourceAccountId),
            beneficiaryId: Number(this.form.beneficiaryId),
            amount: Number(this.form.amount),
            mode: this.form.mode,
            remarks: this.form.remarks
        };
        this.api.transfers.create(d).subscribe({
            next: r => {
                this.success = `Transfer ${r.referenceNumber} completed successfully.`;
                this.form = {...this.form, amount: '', remarks: ''};
                this.load()
            }, error: e => {
                this.error = e?.error?.message || 'Transfer failed. Check account, beneficiary and balance.';
                this.busy = false
            }, complete: () => this.busy = false
        })
    }
}
