import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { vi } from 'vitest';

import { Alunos } from './alunos';
import { AlunoService } from '../../services/aluno.service';

describe('Alunos', () => {

  let component: Alunos;

  const alunoService = {
    buscarAlunos: vi.fn()
  };

  const router = {
    navigate: vi.fn()
  };

  beforeEach(() => {

    TestBed.configureTestingModule({
      imports: [Alunos],

      providers: [
        {
          provide: AlunoService,
          useValue: alunoService
        },
        {
          provide: Router,
          useValue: router
        }
      ]
    });

    component =
      TestBed.createComponent(Alunos)
        .componentInstance;

    vi.clearAllMocks();
  });

  it('deve criar o componente', () => {
    expect(component).toBeTruthy();
  });

  it('deve carregar os alunos', () => {

    alunoService.buscarAlunos.mockReturnValue(
      of({
        content: [
          {
            id: 1,
            matricula: '20260001',
            nome: 'João Silva',
            status: 'ATIVO'
          }
        ],
        totalElements: 1,
        totalPages: 1
      })
    );

    component.carregarAlunos();

    expect(component.alunos().length).toBe(1);

    expect(component.alunos()[0].nome)
      .toBe('João Silva');
  });

  it('deve mostrar erro quando não conseguir carregar os alunos', () => {

    alunoService.buscarAlunos.mockReturnValue(
      throwError(() => new Error())
    );

    component.carregarAlunos();

    expect(component.mensagemErro())
      .toBe('Não foi possível carregar os alunos.');
  });

  it('deve voltar para a primeira página ao pesquisar', () => {

    alunoService.buscarAlunos.mockReturnValue(
      of({
        content: [],
        totalElements: 0,
        totalPages: 0
      })
    );

    component.paginaAtual.set(2);

    component.pesquisar('João');

    expect(component.paginaAtual())
      .toBe(0);

    expect(component.busca())
      .toBe('João');
  });

  it('deve alterar o status do filtro', () => {

    alunoService.buscarAlunos.mockReturnValue(
      of({
        content: [],
        totalElements: 0,
        totalPages: 0
      })
    );

    component.selecionarStatus('INATIVO');

    expect(component.statusSelecionado())
      .toBe('INATIVO');

    expect(component.paginaAtual())
      .toBe(0);
  });

});