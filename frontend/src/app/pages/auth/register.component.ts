import { CommonModule } from '@angular/common';
import {Component} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import {AuthService} from '../../core/auth.service';

@Component({
    selector: 'app-register', standalone: true, imports: [CommonModule, FormsModule, RouterLink], template: `
<div class="auth-page"><div class="auth-art"><a routerLink="/login" class="brand light"><span class="brand-mark">B</span><span>Bank<span>Ease</span></span></a><div class="art-copy"><span class="eyebrow">JOIN BANKEASE</span><h1>Start with a smarter digital banking workspace.</h1><p>Create your account and manage everyday banking from one secure dashboard.</p></div></div>
<div class="auth-panel"><div class="auth-box wide"><span class="eyebrow dark">GET STARTED</span><h1>Create account</h1><p class="muted">Use the same details you will use to sign in.</p><div class="alert error" *ngIf="error">{{error}}</div><form (ngSubmit)="submit()"><div class="form-grid"><label>First name<input name="firstName" [(ngModel)]="form.firstName" required></label><label>Last name<input name="lastName" [(ngModel)]="form.lastName" required></label></div><label>Email<input type="email" name="email" [(ngModel)]="form.email" required></label><label>Phone number<input name="phoneNumber" [(ngModel)]="form.phoneNumber" required pattern="[6-9][0-9]{9}" placeholder="10-digit mobile number"></label><label>Password<input type="password" name="password" [(ngModel)]="form.password" required minlength="8"></label><button class="primary full" [disabled]="busy">{{busy?'Creating...':'Create account'}}</button></form><p class="switch">Already have an account? <a routerLink="/login">Sign in</a></p></div></div></div>`
})
export class RegisterComponent {
    busy = false;
    error = '';
    form = {firstName: '', lastName: '', email: '', password: '', phoneNumber: ''};

    constructor(private auth: AuthService, private router: Router) {
    }

    submit() {
        this.busy = true;
        this.error = '';
        this.auth.register(this.form).subscribe({
            next: r => {
                if (r?.token) this.router.navigateByUrl('/dashboard'); else this.router.navigateByUrl('/login')
            }, error: e => {
                this.error = e?.error?.message || 'Registration failed.';
                this.busy = false
            }, complete: () => this.busy = false
        });
    }
}
