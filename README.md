# Sistema de Gestão de Alunos

Sistema Full Stack para gerenciamento de alunos, desenvolvido utilizando Angular no frontend e Spring Boot no backend.

## Tecnologias

### Backend

- Java
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- H2
- Maven

### Frontend

- Angular
- TypeScript
- HTML
- SCSS

### Testes e Versionamento

- Bruno
- Git
- GitHub

---

# Estrutura do Projeto

## Backend

```
backend/
└── gestao-alunos-api/
    └── src/
        └── main/
            ├── java/
            │   └── io.github.maricruzsantos31.gestaoalunos/
            │       ├── config/
            │       ├── controller/
            │       ├── dto/
            │       ├── entity/
            │       ├── enums/
            │       ├── repository/
            │       ├── security/
            │       ├── service/
            │       ├── GlobalExceptionHandler
            │       └── GestaoAlunosApiApplication
            └── resources/
                └── application.properties
```

## Frontend

```
frontend/
└── src/
    └── app/
        ├── guards/
        ├── interceptors/
        ├── pages/
        │   ├── alunos/
        │   ├── detalhes-aluno/
        │   ├── login/
        │   └── novo-aluno/
        ├── services/
        ├── app.config.ts
        ├── app.routes.ts
        └── app.ts
```

---

# Como Executar o Projeto

## Backend

1. Abra a pasta **backend/gestao-alunos-api** no IntelliJ IDEA.
2. Execute a classe:

```
GestaoAlunosApiApplication
```

A API será iniciada em:

```
http://localhost:8080
```

## Frontend

Abra um terminal na pasta do projeto e execute:

```
cd frontend
npm start
```

O sistema ficará disponível em:

```
http://localhost:4200
```

> Caso seja a primeira execução do projeto, instale as dependências com `npm install`. Nas próximas execuções, basta utilizar `npm start`.

---

# Acesso ao Sistema

## Administrador

- **Usuário:** admin001
- **Senha:** Admin123

## Leitor

- **Usuário:** leitor01
- **Senha:** Leitor123

---

# Autenticação

O sistema utiliza autenticação com JWT.

Após realizar o login, a API gera um token que é utilizado nas requisições autenticadas.

Fluxo de autenticação:

```
Login
   ↓
Validação do usuário
   ↓
Geração do Token JWT
   ↓
Requisições autenticadas
```

---

# Testes da API

Os endpoints da API foram testados utilizando o **Bruno**.

Após realizar o login, o token JWT retornado é utilizado nas requisições autenticadas.

O token deve ser enviado no cabeçalho:

```
Authorization: Bearer SEU_TOKEN
```

---

# Banco de Dados

O projeto utiliza o banco de dados **H2**.

O frontend não acessa diretamente o banco de dados. Toda a comunicação é realizada por meio da API Spring Boot.

Fluxo da aplicação:

```
Frontend (Angular)
        ↓
API Spring Boot
        ↓
Banco H2
```

---

# Funcionalidades

- Login de usuários
- Autenticação com JWT
- Controle de acesso por perfil
- Listagem de alunos
- Busca de alunos
- Filtro por status
- Paginação
- Visualização de detalhes do aluno
- Cadastro de aluno
- Edição de aluno
- Alteração de status
- Validação de dados
- Tratamento de erros da API

---

# Execução Rápida

1. Execute o backend pelo IntelliJ IDEA.
2. Verifique se a API está disponível em:

```
http://localhost:8080
```

3. Abra um terminal na pasta `frontend`.
4. Execute:

```
npm start
```

5. Acesse:

```
http://localhost:4200
```

6. Entre no sistema utilizando um dos usuários informados acima.