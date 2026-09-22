package io.github.maricruzsantos31.gestaoalunos.entity;

import io.github.maricruzsantos31.gestaoalunos.enums.StatusAluno;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "alunos")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String matricula;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 11, updatable = false)
    private String cpf;

    @Column(nullable = false, length = 11)
    private String telefone;

    @Lob
    private byte[] foto;

    @Column(name = "foto_content_type")
    private String fotoContentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAluno status = StatusAluno.ATIVO;

    @Column(nullable = false)
    private boolean excluido = false;

    public Aluno() {
    }

    public Aluno(
            String matricula,
            String nome,
            String email,
            String cpf,
            String telefone) {

        this.matricula = matricula;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
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

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getFotoContentType() {
        return fotoContentType;
    }

    public void setFotoContentType(String fotoContentType) {
        this.fotoContentType = fotoContentType;
    }

    public StatusAluno getStatus() {
        return status;
    }

    public void setStatus(StatusAluno status) {
        this.status = status;
    }

    public boolean isExcluido() {
        return excluido;
    }

    public void setExcluido(boolean excluido) {
        this.excluido = excluido;
    }
}