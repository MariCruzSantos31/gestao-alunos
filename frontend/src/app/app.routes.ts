import { Routes } from '@angular/router';

import { Login } from './pages/login/login';
import { Alunos } from './pages/alunos/alunos';
import { NovoAluno } from './pages/novo-aluno/novo-aluno';
import { DetalhesAluno } from './pages/detalhes-aluno/detalhes-aluno';
import { AlunoEditar } from './pages/alunos/aluno-editar/aluno-editar';

import { authGuard } from './guards/auth.guard';

export const routes: Routes = [

  {
    path: 'login',
    component: Login
  },

  {
    path: 'alunos',
    component: Alunos,
    canActivate: [authGuard]
  },

  {
    path: 'alunos/editar/:id',
    component: AlunoEditar,
    canActivate: [authGuard]
  },

  {
    path: 'alunos/:id',
    component: DetalhesAluno,
    canActivate: [authGuard]
  },

  {
    path: 'novo-aluno',
    component: NovoAluno,
    canActivate: [authGuard]
  },

  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  }

];