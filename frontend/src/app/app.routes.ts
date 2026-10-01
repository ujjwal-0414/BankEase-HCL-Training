import {Routes} from '@angular/router';
import {adminGuard, authGuard} from './core/auth.guard';
import {ShellComponent} from './layout/shell.component';
import {LoginComponent} from './pages/auth/login.component';
import {RegisterComponent} from './pages/auth/register.component';
import {DashboardComponent} from './pages/dashboard/dashboard.component';
import {AccountsComponent} from './pages/accounts/accounts.component';
import {BeneficiariesComponent} from './pages/beneficiaries/beneficiaries.component';
import {TransfersComponent} from './pages/transfers/transfers.component';
import {BillsComponent} from './pages/bills/bills.component';
import {LoansComponent} from './pages/loans/loans.component';
import {InvestmentsComponent} from './pages/investments/investments.component';
import {AdminComponent} from './pages/admin/admin.component';

export const routes: Routes = [
    {path: 'login', component: LoginComponent},
    {path: 'register', component: RegisterComponent},
    {
        path: '', component: ShellComponent, canActivate: [authGuard], children: [
            {path: 'dashboard', component: DashboardComponent},
            {path: 'accounts', component: AccountsComponent},
            {path: 'beneficiaries', component: BeneficiariesComponent},
            {path: 'transfers', component: TransfersComponent},
            {path: 'bills', component: BillsComponent},
            {path: 'loans', component: LoansComponent},
            {path: 'investments', component: InvestmentsComponent},
            {path: 'admin', component: AdminComponent, canActivate: [adminGuard]}
        ]
    },
    {path: '', pathMatch: 'full', redirectTo: 'dashboard'},
    {path: '**', redirectTo: 'dashboard'}
];
