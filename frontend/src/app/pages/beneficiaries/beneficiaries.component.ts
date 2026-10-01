import {CommonModule} from '@angular/common';
import {Component, OnInit} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {ApiService} from '../../core/api.service';
import {Beneficiary} from '../../core/models';
import {EmptyComponent, SpinnerComponent, StatusComponent} from '../../shared/ui';

@Component({
    selector: 'app-beneficiaries',
    standalone: true,
    imports: [CommonModule, FormsModule, SpinnerComponent, EmptyComponent, StatusComponent],
    template: `<div class="page-head"><div><span class="eyebrow dark">PAYEES</span><h1>Beneficiaries</h1><p>Manage saved destinations for secure transfers.</p></div><button class="primary" (click)="show=true">+ Add beneficiary</button></div>@if(error){<div class="alert error">{{error}}</div>}@if(loading){<app-spinner/>}@else{<div class="beneficiary-grid">@for(b of items;track b.id){<div class="card beneficiary"><div class="benef-top"><div class="benef-avatar">{{b.name.charAt(0)}}</div><app-status [status]="b.active?'ACTIVE':'INACTIVE'"/></div><h3>{{b.nickname||b.name}}</h3><p>{{b.name}}</p><div class="detail-line"><span>Account</span><b>{{b.accountNumber}}</b></div><div class="detail-line"><span>IFSC</span><b>{{b.ifscCode}}</b></div><div class="detail-line"><span>Bank</span><b>{{b.bankName||'—'}}</b></div><button class="danger ghost full" (click)="remove(b.id)">Remove</button></div>}@empty{<app-empty title="No beneficiaries" text="Add a trusted payee before making transfers."/>}</div>}@if(show){<div class="modal-backdrop"><div class="modal"><div class="modal-head"><div><h2>Add beneficiary</h2><p>Double-check the destination details.</p></div><button class="icon-btn" (click)="show=false">×</button></div><form class="form-stack" (ngSubmit)="create()"><label>Name<input name="name" [(ngModel)]="form.name" required></label><label>Account number<input name="accountNumber" [(ngModel)]="form.accountNumber" required></label><label>IFSC<input name="ifscCode" [(ngModel)]="form.ifscCode" required></label><label>Bank name<input name="bankName" [(ngModel)]="form.bankName"></label><label>Nickname<input name="nickname" [(ngModel)]="form.nickname"></label><button class="primary full" [disabled]="busy">{{busy?'Saving...':'Save beneficiary'}}</button></form></div></div>}`
})
export class BeneficiariesComponent implements OnInit {
    items: Beneficiary[] = [];
    loading = true;
    show = false;
    busy = false;
    error = '';
    form: any = {name: '', accountNumber: '', ifscCode: '', bankName: '', nickname: ''};

    constructor(private api: ApiService) {
    }

    ngOnInit() {
        this.load()
    }

    load() {
        this.api.beneficiaries.list().subscribe({
            next: r => this.items = r,
            error: e => this.error = e?.error?.message || 'Unable to load beneficiaries.',
            complete: () => this.loading = false
        })
    }

    create() {
        this.busy = true;
        this.api.beneficiaries.create(this.form).subscribe({
            next: () => {
                this.show = false;
                this.form = {name: '', accountNumber: '', ifscCode: '', bankName: '', nickname: ''};
                this.load()
            }, error: e => {
                this.error = e?.error?.message || 'Could not add beneficiary.';
                this.busy = false
            }, complete: () => this.busy = false
        })
    }

    remove(id: number) {
        if (!confirm('Remove this beneficiary?')) return;
        this.api.beneficiaries.remove(id).subscribe({
            next: () => this.load(),
            error: e => this.error = e?.error?.message || 'Could not remove beneficiary.'
        })
    }
}
