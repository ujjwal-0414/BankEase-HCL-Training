import {CommonModule} from '@angular/common';
import {Component} from '@angular/core';
import {Router, RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {AuthService} from '../core/auth.service';

@Component({
    selector: 'app-shell',
    standalone: true,
    imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
    template: `
<div class="app-shell">
 <aside class="sidebar">
  <a class="brand" routerLink="/dashboard"><span class="brand-mark">B</span><span>Bank<span>Ease</span></span></a>
  <div class="side-label">BANKING</div>
  <nav>
   <a routerLink="/dashboard" routerLinkActive="active" [routerLinkActiveOptions]="{exact:true}" class="nav-item"><span>⌂</span>Overview</a>
   <a routerLink="/accounts" routerLinkActive="active" class="nav-item"><span>▣</span>Accounts</a>
   <a routerLink="/beneficiaries" routerLinkActive="active" class="nav-item"><span>◇</span>Beneficiaries</a>
   <a routerLink="/transfers" routerLinkActive="active" class="nav-item"><span>⇄</span>Transfers</a>
   <a routerLink="/bills" routerLinkActive="active" class="nav-item"><span>▤</span>Bill payments</a>
   <a routerLink="/loans" routerLinkActive="active" class="nav-item"><span>◫</span>Loans</a>
   <a routerLink="/investments" routerLinkActive="active" class="nav-item"><span>◈</span>Investments</a>
  </nav>
  @if(auth.user()?.role==='ADMIN'){<div class="side-label">ADMINISTRATION</div><a routerLink="/admin" routerLinkActive="active" class="nav-item"><span>⚙</span>Admin center</a>}
  <div class="side-bottom"><div class="secure"><span>●</span><div><b>Secure session</b><small>JWT protected</small></div></div><button class="logout" (click)="logout()">↪ <span>Sign out</span></button></div>
 </aside>
 <main class="main-area"><header class="topbar"><div><span class="crumb">BankEase</span><span class="sep">/</span><strong>Digital Banking</strong></div><div class="profile"><div class="avatar">{{(auth.user()?.name||'U').charAt(0)}}</div><div><b>{{auth.user()?.name||'User'}}</b><small>{{auth.user()?.role||'USER'}}</small></div></div></header><div class="content"><router-outlet/></div></main>
</div>`
})
export class ShellComponent {
    constructor(public auth: AuthService, private router: Router) {
    }

    logout() {
        this.auth.logout();
        this.router.navigateByUrl('/login');
    }
}
