import { TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { vi } from 'vitest';

import { Login } from './login';
import { AuthService } from '../../services/auth.service';

describe('Login', () => {

  let component: Login;

  const authService = {
    fazerLogin: vi.fn()
  };

  const router = {
    navigate: vi.fn()
  };

  beforeEach(() => {

    TestBed.configureTestingModule({
      imports: [Login],

      providers: [
        {
          provide: AuthService,
          useValue: authService
        },
        {
          provide: Router,
          useValue: router
        },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              queryParamMap: {
                get: () => null
              }
            }
          }
        }
      ]
    });

    component =
      TestBed.createComponent(Login)
        .componentInstance;

    vi.clearAllMocks();
  });

  it('deve criar o componente', () => {
    expect(component).toBeTruthy();
  });

  it('deve iniciar o formulário inválido', () => {
    expect(component.loginForm.invalid).toBe(true);
  });

  it('deve fazer login com dados válidos', () => {

    authService.fazerLogin.mockReturnValue(
      of({
        token: 'token-teste',
        perfil: 'ADMIN'
      })
    );

    component.loginForm.setValue({
      usuario: 'usuario01',
      senha: 'senha123'
    });

    component.fazerLogin();

    expect(authService.fazerLogin)
      .toHaveBeenCalledWith(
        'usuario01',
        'senha123'
      );

    expect(router.navigate)
      .toHaveBeenCalledWith(['/alunos']);
  });

  it('deve mostrar erro quando o login falhar', () => {

    authService.fazerLogin.mockReturnValue(
      throwError(() => new Error())
    );

    component.loginForm.setValue({
      usuario: 'usuario01',
      senha: 'senha123'
    });

    component.fazerLogin();

    expect(component.mensagemErro())
      .toBe('Usuário ou senha inválidos.');
  });

});