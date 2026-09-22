package io.github.maricruzsantos31.gestaoalunos.service;

import io.github.maricruzsantos31.gestaoalunos.entity.Usuario;
import io.github.maricruzsantos31.gestaoalunos.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }
}