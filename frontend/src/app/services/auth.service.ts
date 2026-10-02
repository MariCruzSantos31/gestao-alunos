import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface LoginResponse {
  token: string;
  perfil: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly apiUrl = 'http://localhost:8080/api/auth/login';

  constructor(
    private http: HttpClient
  ) {}

  fazerLogin(
    usuario: string,
    senha: string
  ): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      this.apiUrl,
      {
        username: usuario,
        password: senha
      }
    );
  }
}