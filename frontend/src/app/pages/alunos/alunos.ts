import { Component, OnInit, computed, signal } from '@angular/core';
import { Router } from '@angular/router';

import {
  AlunoService,
  AlunoListagem,
  FiltroStatus
} from '../../services/aluno.service';

@Component({
  selector: 'app-alunos',
  imports: [],
  templateUrl: './alunos.html',
  styleUrl: './alunos.scss'
})
export class Alunos implements OnInit {

  readonly tamanhoPagina = 10;
  readonly ehAdmin = sessionStorage.getItem('perfil') === 'ADMIN';

  private readonly atrasoBusca = 400;
  private temporizadorBusca?: number;

  alunos = signal<AlunoListagem[]>([]);
  carregando = signal(false);
  mensagemErro = signal('');

  busca = signal('');
  statusSelecionado = signal<FiltroStatus>('ATIVO');
  filtroAberto = signal(false);

  paginaAtual = signal(0);
  totalPaginas = signal(0);
  totalElementos = signal(0);

  modalExclusao = signal(false);
  alunoParaExcluir = signal<number | null>(null);

  paginas = computed(() =>
    Array.from(
      { length: this.totalPaginas() },
      (_, indice) => indice
    )
  );

  rotuloStatus = computed(() => {
    const status = this.statusSelecionado();

    if (status === 'TODOS') {
      return 'Todos';
    }

    if (status === 'INATIVO') {
      return 'Inativos';
    }

    return 'Ativos';
  });

  constructor(
    private alunoService: AlunoService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carregarAlunos();
  }

  carregarAlunos(): void {
    this.carregando.set(true);
    this.mensagemErro.set('');

    this.alunoService
      .buscarAlunos(
        this.paginaAtual(),
        this.tamanhoPagina,
        this.statusSelecionado(),
        this.busca()
      )
      .subscribe({
        next: resposta => {
          this.alunos.set(resposta.content);
          this.totalPaginas.set(resposta.totalPages);
          this.totalElementos.set(resposta.totalElements);
          this.carregando.set(false);
        },

        error: () => {
          this.mensagemErro.set(
            'Não foi possível carregar os alunos.'
          );
          this.carregando.set(false);
        }
      });
  }

  pesquisarAoDigitar(valor: string): void {
    if (this.temporizadorBusca) {
      window.clearTimeout(this.temporizadorBusca);
    }

    this.temporizadorBusca = window.setTimeout(() => {
      this.pesquisar(valor);
    }, this.atrasoBusca);
  }

  pesquisar(valor: string): void {
    if (this.temporizadorBusca) {
      window.clearTimeout(this.temporizadorBusca);
      this.temporizadorBusca = undefined;
    }

    this.busca.set(valor.trim());
    this.paginaAtual.set(0);

    this.carregarAlunos();
  }

  alternarFiltro(): void {
    this.filtroAberto.update(aberto => !aberto);
  }

  selecionarStatus(status: FiltroStatus): void {
    this.statusSelecionado.set(status);
    this.filtroAberto.set(false);
    this.paginaAtual.set(0);

    this.carregarAlunos();
  }

  irParaPagina(pagina: number): void {
    if (pagina < 0 || pagina >= this.totalPaginas()) {
      return;
    }

    this.paginaAtual.set(pagina);
    this.carregarAlunos();
  }

  novoAluno(): void {
    this.router.navigate(['/novo-aluno']);
  }

  verDetalhes(id: number): void {
    this.router.navigate(['/alunos', id]);
  }

  editarAluno(id: number): void {
    this.router.navigate(['/alunos/editar', id]);
  }

  abrirModalExclusao(id: number): void {
    this.alunoParaExcluir.set(id);
    this.modalExclusao.set(true);
  }

  fecharModalExclusao(): void {
    this.alunoParaExcluir.set(null);
    this.modalExclusao.set(false);
  }

  confirmarExclusao(): void {
    const id = this.alunoParaExcluir();

    if (id === null) {
      return;
    }

    this.fecharModalExclusao();

    this.alunoService.excluirAluno(id).subscribe({
      next: () => {
        this.carregarAlunos();
      },

      error: () => {
        this.mensagemErro.set(
          'Não foi possível excluir o aluno.'
        );
      }
    });
  }

  sair(): void {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('perfil');

    this.router.navigate(['/login']);
  }
}