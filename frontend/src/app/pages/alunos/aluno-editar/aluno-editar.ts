import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import {
  AlunoService,
  AlunoDetalhes,
  AlunoAtualizacao
} from '../../../services/aluno.service';

@Component({
  selector: 'app-aluno-editar',
  imports: [FormsModule],
  templateUrl: './aluno-editar.html',
  styleUrl: './aluno-editar.scss'
})
export class AlunoEditar implements OnInit {

  id = 0;

  carregando = signal(true);
  salvando = signal(false);
  mostrarModalSaida = signal(false);

  mensagemErro = signal('');
  mensagemSucesso = signal('');

  nome = '';
  email = '';
  cpf = '';
  telefone = '';
  matricula = '';
  status: 'ATIVO' | 'INATIVO' = 'ATIVO';

  foto?: string;
  fotoContentType?: string;

  private dadosOriginais = {
    nome: '',
    email: '',
    telefone: '',
    status: 'ATIVO' as 'ATIVO' | 'INATIVO',
    foto: undefined as string | undefined,
    fotoContentType: undefined as string | undefined
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private alunoService: AlunoService
  ) {}

  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));

    if (!this.id) {
      this.mensagemErro.set('Aluno não encontrado.');
      this.carregando.set(false);
      return;
    }

    this.carregarAluno();
  }

  carregarAluno(): void {
    this.alunoService.buscarAlunoPorId(this.id).subscribe({
      next: (aluno: AlunoDetalhes) => {
        this.nome = aluno.nome;
        this.email = aluno.email;
        this.cpf = aluno.cpf;
        this.telefone = aluno.telefone;
        this.matricula = aluno.matricula;
        this.status = aluno.status;
        this.foto = aluno.foto;
        this.fotoContentType = aluno.fotoContentType;

        this.salvarDadosOriginais();
        this.carregando.set(false);
      },

      error: () => {
        this.mensagemErro.set(
          'Não foi possível carregar os dados do aluno.'
        );
        this.carregando.set(false);
      }
    });
  }

  private salvarDadosOriginais(): void {
    this.dadosOriginais = {
      nome: this.nome,
      email: this.email,
      telefone: this.telefone,
      status: this.status,
      foto: this.foto,
      fotoContentType: this.fotoContentType
    };
  }

  temAlteracoes(): boolean {
    return (
      this.nome !== this.dadosOriginais.nome ||
      this.email !== this.dadosOriginais.email ||
      this.telefone !== this.dadosOriginais.telefone ||
      this.status !== this.dadosOriginais.status ||
      this.foto !== this.dadosOriginais.foto ||
      this.fotoContentType !== this.dadosOriginais.fotoContentType
    );
  }

  nomeValido(): boolean {
    const nome = this.nome.trim();
    return nome.length >= 3 && nome.length <= 120;
  }

  emailValido(): boolean {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.email);
  }

  telefoneValido(): boolean {
    return /^\d{10,11}$/.test(this.telefone);
  }

  formularioValido(): boolean {
    return (
      this.nomeValido() &&
      this.emailValido() &&
      this.telefoneValido()
    );
  }

  salvar(): void {
    if (
      !this.temAlteracoes() ||
      !this.formularioValido() ||
      this.salvando()
    ) {
      return;
    }

    this.mensagemErro.set('');
    this.mensagemSucesso.set('');
    this.salvando.set(true);

    const aluno: AlunoAtualizacao = {
      nome: this.nome.trim(),
      email: this.email.trim(),
      telefone: this.telefone.trim(),
      status: this.status
    };

    this.alunoService.atualizarAluno(this.id, aluno).subscribe({
      next: () => {
        this.salvando.set(false);
        this.mensagemSucesso.set('Aluno atualizado com sucesso.');

        this.salvarDadosOriginais();

        setTimeout(() => {
          this.router.navigate(['/alunos']);
        }, 1200);
      },

      error: () => {
        this.salvando.set(false);
        this.mensagemErro.set(
          'Não foi possível atualizar o aluno.'
        );
      }
    });
  }

  voltar(): void {
    if (this.temAlteracoes()) {
      this.mostrarModalSaida.set(true);
      return;
    }

    this.router.navigate(['/alunos']);
  }

  fecharModalSaida(): void {
    this.mostrarModalSaida.set(false);
  }

  sairSemSalvar(): void {
    this.mostrarModalSaida.set(false);
    this.router.navigate(['/alunos']);
  }
}