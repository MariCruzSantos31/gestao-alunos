import { ChangeDetectorRef, Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AlunoService } from '../../services/aluno.service';

@Component({
  selector: 'app-novo-aluno',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './novo-aluno.html',
  styleUrl: './novo-aluno.scss'
})
export class NovoAluno {

  nome = '';
  cpf = '';
  email = '';
  telefone = '';

  erros: { [campo: string]: string } = {};
  mensagemSucesso = '';
  mensagemErro = '';
  matriculaGerada = '';

  cadastrando = false;
  mostrarConfirmacaoSaida = false;

  constructor(
    private router: Router,
    private alunoService: AlunoService,
    private cdr: ChangeDetectorRef
  ) {}

  voltar(): void {
    if (this.temAlteracoesNaoSalvas()) {
      this.mostrarConfirmacaoSaida = true;
      return;
    }

    this.router.navigate(['/alunos']);
  }

  temAlteracoesNaoSalvas(): boolean {
    return !!(
      this.nome.trim() ||
      this.cpf.trim() ||
      this.email.trim() ||
      this.telefone.trim()
    );
  }

  cancelarSaida(): void {
    this.mostrarConfirmacaoSaida = false;
  }

  confirmarSaida(): void {
    this.mostrarConfirmacaoSaida = false;
    this.router.navigate(['/alunos']);
  }

  validarNome(): void {
    const valor = this.nome.trim();

    if (!valor) {
      this.erros['nome'] = 'Campo obrigatório';
    } else if (valor.length < 3 || valor.length > 120) {
      this.erros['nome'] = 'Nome deve ter entre 3 e 120 caracteres';
    } else {
      delete this.erros['nome'];
    }
  }

  validarCpf(): void {
    const valor = this.cpf.trim();

    if (!valor) {
      this.erros['cpf'] = 'Campo obrigatório';
      return;
    }

    if (!/^\d{11}$/.test(valor)) {
      this.erros['cpf'] = 'CPF deve conter 11 dígitos';
      return;
    }

    if (!this.isCpfValido(valor)) {
      this.erros['cpf'] = 'CPF inválido';
      return;
    }

    delete this.erros['cpf'];

    this.alunoService.verificarCpf(valor).subscribe({
      next: cadastrado => {
        if (cadastrado) {
          this.erros['cpf'] = 'CPF já cadastrado.';
        }
        this.cdr.detectChanges();
      }
    });
  }

  isCpfValido(cpf: string): boolean {
    if (!/^\d{11}$/.test(cpf) || /^(\d)\1{10}$/.test(cpf)) {
      return false;
    }

    let soma = 0;

    for (let i = 0; i < 9; i++) {
      soma += Number(cpf[i]) * (10 - i);
    }

    let resto = soma % 11;
    const primeiro = resto < 2 ? 0 : 11 - resto;

    if (primeiro !== Number(cpf[9])) {
      return false;
    }

    soma = 0;

    for (let i = 0; i < 10; i++) {
      soma += Number(cpf[i]) * (11 - i);
    }

    resto = soma % 11;
    const segundo = resto < 2 ? 0 : 11 - resto;

    return segundo === Number(cpf[10]);
  }

  validarEmail(): void {
    const valor = this.email.trim();

    if (!valor) {
      this.erros['email'] = 'Campo obrigatório';
      return;
    }

    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(valor)) {
      this.erros['email'] = 'E-mail inválido';
      return;
    }

    delete this.erros['email'];

    this.alunoService.verificarEmail(valor).subscribe({
      next: cadastrado => {
        if (cadastrado) {
          this.erros['email'] = 'E-mail já cadastrado.';
        }
        this.cdr.detectChanges();
      }
    });
  }

  validarTelefone(): void {
    const valor = this.telefone.trim();

    if (!valor) {
      this.erros['telefone'] = 'Campo obrigatório';
    } else if (!/^\d{10,11}$/.test(valor)) {
      this.erros['telefone'] =
        'Telefone deve conter DDD e número';
    } else {
      delete this.erros['telefone'];
    }
  }

  limparErro(campo: string): void {
    delete this.erros[campo];
    this.mensagemErro = '';
  }

  formularioValido(): boolean {
    return (
      this.nome.trim().length >= 3 &&
      this.nome.trim().length <= 120 &&
      this.isCpfValido(this.cpf.trim()) &&
      /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.email.trim()) &&
      /^\d{10,11}$/.test(this.telefone.trim()) &&
      !this.erros['cpf'] &&
      !this.erros['email']
    );
  }

  incluirAluno(): void {
    if (this.cadastrando) {
      return;
    }

    this.validarNome();
    this.validarCpf();
    this.validarEmail();
    this.validarTelefone();

    if (!this.formularioValido()) {
      this.cdr.detectChanges();
      return;
    }

    this.erros = {};
    this.mensagemErro = '';
    this.cadastrando = true;

    this.alunoService.cadastrarAluno({
      nome: this.nome,
      email: this.email,
      cpf: this.cpf,
      telefone: this.telefone
    }).subscribe({
      next: resposta => {
        this.cadastrando = false;
        this.mensagemSucesso =
          'Cadastro realizado com sucesso!';
        this.matriculaGerada = resposta.matricula;
        this.cdr.detectChanges();
      },

      error: erro => {
        this.cadastrando = false;

        if (erro?.status === 409) {
          const mensagem = erro?.error?.mensagem || '';

          if (mensagem.toLowerCase().includes('e-mail')) {
            this.erros['email'] =
              mensagem || 'E-mail já cadastrado.';
          } else {
            this.erros['cpf'] =
              mensagem || 'CPF já cadastrado.';
          }
        } else if (erro?.status === 400 && erro?.error) {
          this.erros = { ...erro.error };
        } else {
          this.mensagemErro =
            'Não foi possível realizar o cadastro.';
        }

        this.cdr.detectChanges();
      }
    });
  }

  fecharMensagemSucesso(): void {
    this.mensagemSucesso = '';
    this.matriculaGerada = '';
    this.router.navigate(['/alunos']);
  }
}