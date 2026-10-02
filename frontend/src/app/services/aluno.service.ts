import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface AlunoListagem {
  id: number;
  matricula: string;
  nome: string;
  status: 'ATIVO' | 'INATIVO';
}

export interface AlunoDetalhes {
  id: number;
  matricula: string;
  nome: string;
  email: string;
  cpf: string;
  telefone: string;
  status: 'ATIVO' | 'INATIVO';
  excluido: boolean;
  foto?: string;
  fotoContentType?: string;
}

export interface AlunoCadastro {
  nome: string;
  email: string;
  cpf: string;
  telefone: string;
  foto?: string;
  fotoContentType?: string;
}

export interface AlunoAtualizacao {
  nome: string;
  email: string;
  telefone: string;
  foto?: string;
  fotoContentType?: string;
  status: 'ATIVO' | 'INATIVO';
}

export interface PaginaAlunos {
  content: AlunoListagem[];
  totalElements: number;
  totalPages: number;
}

export type FiltroStatus = 'TODOS' | 'ATIVO' | 'INATIVO';

@Injectable({
  providedIn: 'root'
})
export class AlunoService {

  private readonly apiUrl = 'http://localhost:8080/api/alunos';

  constructor(private http: HttpClient) {}

  buscarAlunos(
    pagina: number,
    tamanhoPagina: number,
    status: FiltroStatus,
    busca: string
  ): Observable<PaginaAlunos> {

    let params = new HttpParams()
      .set('page', pagina.toString())
      .set('size', tamanhoPagina.toString())
      .set('status', status);

    if (busca) {
      params = params.set('busca', busca);
    }

    return this.http.get<PaginaAlunos>(
      this.apiUrl,
      { params }
    );
  }

  buscarAlunoPorId(id: number): Observable<AlunoDetalhes> {
    return this.http.get<AlunoDetalhes>(
      `${this.apiUrl}/${id}`
    );
  }

  verificarCpf(cpf: string): Observable<boolean> {
    return this.http.get<boolean>(
      `${this.apiUrl}/cpf/${cpf}`
    );
  }

  verificarEmail(email: string): Observable<boolean> {
    return this.http.get<boolean>(
      `${this.apiUrl}/email/${encodeURIComponent(email)}`
    );
  }

  cadastrarAluno(
    aluno: AlunoCadastro
  ): Observable<AlunoListagem> {

    return this.http.post<AlunoListagem>(
      this.apiUrl,
      aluno
    );
  }

  atualizarAluno(
    id: number,
    aluno: AlunoAtualizacao
  ): Observable<AlunoListagem> {

    return this.http.put<AlunoListagem>(
      `${this.apiUrl}/${id}`,
      aluno
    );
  }

  excluirAluno(id: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${id}`
    );
  }
}