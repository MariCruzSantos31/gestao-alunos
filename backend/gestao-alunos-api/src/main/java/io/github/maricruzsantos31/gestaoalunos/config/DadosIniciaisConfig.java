package io.github.maricruzsantos31.gestaoalunos.config;

import io.github.maricruzsantos31.gestaoalunos.entity.Usuario;
import io.github.maricruzsantos31.gestaoalunos.enums.Perfil;
import io.github.maricruzsantos31.gestaoalunos.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DadosIniciaisConfig {

    @Bean
    public CommandLineRunner carregarUsuariosIniciais(
            UsuarioService usuarioService,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (usuarioService.buscarPorUsername("admin001").isEmpty()) {

                Usuario admin = new Usuario(
                        "admin001",
                        passwordEncoder.encode("Admin123"),
                        Perfil.ADMIN
                );

                usuarioService.salvar(admin);
            }

            if (usuarioService.buscarPorUsername("leitor01").isEmpty()) {

                Usuario leitor = new Usuario(
                        "leitor01",
                        passwordEncoder.encode("Leitor123"),
                        Perfil.LEITOR
                );

                usuarioService.salvar(leitor);
            }
        };
    }
}