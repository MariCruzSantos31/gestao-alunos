package io.github.maricruzsantos31.gestaoalunos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AlunoCadastroRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(
            min = 3,
            max = 120,
            message = "Nome deve ter entre 3 e 120 caracteres"
    )
    private String nome;

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(
            regexp = "\\d{11}",
            message = "CPF deve conter 11 dígitos"
    )
    private String cpf;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(
            regexp = "\\d{10,11}",
            message = "Telefone deve conter DDD e número"
    )
    private String telefone;

    private byte[] foto;

    private String fotoContentType;

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

    public void setCpf(String cpf) {
        this.cpf = cpf;
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
}