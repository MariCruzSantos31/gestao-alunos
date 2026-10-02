import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import {
  AlunoService,
  AlunoDetalhes
} from '../../services/aluno.service';

@Component({
  selector: 'app-detalhes-aluno',
  imports: [],
  templateUrl: './detalhes-aluno.html',
  styleUrl: './detalhes-aluno.scss'
})
export class DetalhesAluno implements OnInit {

  aluno = signal<AlunoDetalhes | null>(null);
  carregando = signal(true);
  mensagemErro = signal('');

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private alunoService: AlunoService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    if (!id) {
      this.mensagemErro.set('Aluno não encontrado.');
      this.carregando.set(false);
      return;
    }

    this.alunoService.buscarAlunoPorId(id).subscribe({
      next: (aluno) => {
        this.aluno.set(aluno);
        this.carregando.set(false);
      },

      error: () => {
        this.mensagemErro.set('Aluno não encontrado.');
        this.carregando.set(false);
      }
    });
  }

  voltar(): void {
    this.router.navigate(['/alunos']);
  }
}