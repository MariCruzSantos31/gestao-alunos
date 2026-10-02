import { Component, signal } from '@angular/core';
import {
  ActivatedRoute,
  Router
} from '@angular/router';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';

import { AuthService } from '../../services/auth.service';

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
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
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
          Validators.pattern(
            /^(?=.*[A-Za-z])(?=.*\d).+$/
          )
        ]
      ]
    });

    const sessao =
      this.route.snapshot.queryParamMap.get('sessao');

    if (sessao === 'expirada') {
      this.mensagemErro.set(
        'Sua sessão expirou. Faça login novamente.'
      );
    }
  }

  alternarVisibilidadeSenha(): void {
    this.mostrarSenha = !this.mostrarSenha;
  }

  fazerLogin(): void {

    if (
      this.loginForm.invalid ||
      this.carregando()
    ) {
      return;
    }

    this.carregando.set(true);
    this.mensagemErro.set('');

    const usuario = this.loginForm.value.usuario;
    const senha = this.loginForm.value.senha;

    this.authService
      .fazerLogin(usuario, senha)
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

          this.router.navigate(['/alunos']);
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