package io.github.maricruzsantos31.gestaoalunos.dto;

import io.github.maricruzsantos31.gestaoalunos.entity.Aluno;
import io.github.maricruzsantos31.gestaoalunos.enums.StatusAluno;

public class AlunoListagemResponse {

    private Long id;
    private String matricula;
    private String nome;
    private StatusAluno status;

    public AlunoListagemResponse(Aluno aluno) {
        this.id = aluno.getId();
        this.matricula = aluno.getMatricula();
        this.nome = aluno.getNome();
        this.status = aluno.getStatus();
    }

    public Long getId() {
        return id;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getNome() {
        return nome;
    }

    public StatusAluno getStatus() {
        return status;
    }
}