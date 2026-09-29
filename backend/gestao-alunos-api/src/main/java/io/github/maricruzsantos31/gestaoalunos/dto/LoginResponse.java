package io.github.maricruzsantos31.gestaoalunos.dto;

public class LoginResponse {

    private final String token;
    private final String perfil;

    public LoginResponse(String token, String perfil) {
        this.token = token;
        this.perfil = perfil;
    }

    public String getToken() {
        return token;
    }

    public String getPerfil() {
        return perfil;
    }
}