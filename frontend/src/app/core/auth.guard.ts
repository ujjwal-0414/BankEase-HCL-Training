import {CanActivateFn, Router} from '@angular/router';
import {inject} from '@angular/core';

export const authGuard: CanActivateFn = () => {
    const router = inject(Router);
    return localStorage.getItem('bankease_token') ? true : router.parseUrl('/login');
};
export const adminGuard: CanActivateFn = () => {
    const router = inject(Router);
    try {
        const u = JSON.parse(localStorage.getItem('bankease_user') || 'null');
        return u?.role === 'ADMIN' ? true : router.parseUrl('/dashboard');
    } catch {
        return router.parseUrl('/dashboard');
    }
};
