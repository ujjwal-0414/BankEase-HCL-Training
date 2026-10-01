import {Injectable, signal} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, tap} from 'rxjs';
import {UserSession} from './models';
import {environment} from '../../environments/environment';

const API = environment.apiBaseUrl;

@Injectable({providedIn: 'root'})
export class AuthService {
    readonly user = signal<UserSession | null>(this.readUser());

    constructor(private http: HttpClient) {
    }

    login(payload: { email: string; password: string }): Observable<UserSession> {
        return this.http.post<UserSession>(`${API}/auth/login`, payload).pipe(tap(r => this.store(r)));
    }

    register(payload: any): Observable<UserSession> {
        return this.http.post<UserSession>(`${API}/auth/register`, payload).pipe(tap(r => {
            if (r?.token) this.store(r)
        }));
    }

    health() {
        return this.http.get(`${API}/auth/health`, {responseType: 'text'});
    }

    logout() {
        localStorage.removeItem('bankease_token');
        localStorage.removeItem('bankease_user');
        this.user.set(null);
    }

    isLoggedIn() {
        return !!localStorage.getItem('bankease_token');
    }

    private store(r: any) {
        const u: UserSession = {userId: r.userId, name: r.name, email: r.email, role: r.role, token: r.token};
        localStorage.setItem('bankease_token', r.token);
        localStorage.setItem('bankease_user', JSON.stringify(u));
        this.user.set(u);
    }

    private readUser(): UserSession | null {
        try {
            return JSON.parse(localStorage.getItem('bankease_user') || 'null')
        } catch {
            return null
        }
    }
}

export const API_BASE = API;
