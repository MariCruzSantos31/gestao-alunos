package io.github.maricruzsantos31.gestaoalunos.repository;

import io.github.maricruzsantos31.gestaoalunos.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);
}