import { Component, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

interface LoginResponse {
  token: string;
  perfil: string;
}

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class Login {

  loginForm: FormGroup;

  mostrarSenha = false;

  carregando = signal(false);

  mensagemErro = signal('');

  constructor(
    private formBuilder: FormBuilder,
    private http: HttpClient
  ) {

    this.loginForm = this.formBuilder.group({
      usuario: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.pattern(/^[a-zA-Z0-9]+$/)
        ]
      ],

      senha: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.maxLength(20),
          Validators.pattern(/^(?=.*[A-Za-z])(?=.*\d).+$/)
        ]
      ]
    });
  }

  alternarVisibilidadeSenha(): void {
    this.mostrarSenha = !this.mostrarSenha;
  }

  fazerLogin(): void {

    if (this.loginForm.invalid || this.carregando()) {
      return;
    }

    this.carregando.set(true);
    this.mensagemErro.set('');

    const dadosLogin = {
      username: this.loginForm.value.usuario,
      password: this.loginForm.value.senha
    };

    this.http
      .post<LoginResponse>(
        'http://localhost:8080/api/auth/login',
        dadosLogin
      )
      .subscribe({

        next: (resposta) => {

          sessionStorage.setItem(
            'token',
            resposta.token
          );

          sessionStorage.setItem(
            'perfil',
            resposta.perfil
          );

          this.carregando.set(false);
        },

        error: () => {

          this.carregando.set(false);

          this.mensagemErro.set(
            'Usuário ou senha inválidos.'
          );
        }
      });
  }
}