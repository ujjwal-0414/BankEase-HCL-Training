import { CommonModule } from '@angular/common';
import {Component} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import {AuthService} from '../../core/auth.service';

@Component({
    selector: 'app-login', standalone: true, imports: [CommonModule, FormsModule, RouterLink], template: `
<div class="auth-page"><div class="auth-art"><a routerLink="/login" class="brand light"><span class="brand-mark">B</span><span>Bank<span>Ease</span></span></a><div class="art-copy"><span class="eyebrow">DIGITAL BANKING</span><h1>One secure place for your everyday finances.</h1><p>Move money, manage accounts, invest and keep your financial life organised with BankEase.</p><div class="art-points"><span>✓ Secure JWT authentication</span><span>✓ Real-time account visibility</span><span>✓ Simple, modern banking experience</span></div></div></div>
<div class="auth-panel"><div class="auth-box"><span class="eyebrow dark">WELCOME BACK</span><h1>Sign in</h1><p class="muted">Enter your BankEase credentials to continue.</p><div class="alert error" *ngIf="error">{{error}}</div><form (ngSubmit)="submit()"><label>Email<input type="email" name="email" [(ngModel)]="email" required autocomplete="email" placeholder="you@example.com"></label><label>Password<input type="password" name="password" [(ngModel)]="password" required autocomplete="current-password" placeholder="••••••••"></label><button class="primary full" [disabled]="busy">{{busy?'Signing in...':'Sign in'}}</button></form><p class="switch">New to BankEase? <a routerLink="/register">Create an account</a></p></div></div></div>`
})
export class LoginComponent {
    email = '';
    password = '';
    busy = false;
    error = '';

    constructor(private auth: AuthService, private router: Router) {
    }

    submit() {
        this.busy = true;
        this.error = '';
        this.auth.login({
            email: this.email,
            password: this.password
        }).subscribe({
            next: () => this.router.navigateByUrl('/dashboard'), error: e => {
                this.error = e?.error?.message || 'Invalid email or password.';
                this.busy = false
            }, complete: () => this.busy = false
        });
    }
}
