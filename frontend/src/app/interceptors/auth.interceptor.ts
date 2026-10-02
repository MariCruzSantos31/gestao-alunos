import { inject } from '@angular/core';
import {
  HttpErrorResponse,
  HttpInterceptorFn
} from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const router = inject(Router);
  const token = sessionStorage.getItem('token');

  const requisicao = token
    ? req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      })
    : req;

  return next(requisicao).pipe(
    catchError((erro: HttpErrorResponse) => {

      console.log('ERRO HTTP:', req.url, erro.status, erro.error);

      const ehLogin = req.url.includes('/api/auth/login');

      if (erro.status === 401 && !ehLogin) {
        sessionStorage.removeItem('token');
        sessionStorage.removeItem('perfil');

        router.navigate(['/login'], {
          queryParams: {
            sessao: 'expirada'
          }
        });
      }

      return throwError(() => erro);
    })
  );
};