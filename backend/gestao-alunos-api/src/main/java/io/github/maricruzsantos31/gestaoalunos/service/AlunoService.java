package io.github.maricruzsantos31.gestaoalunos.service;

import io.github.maricruzsantos31.gestaoalunos.entity.Aluno;
import io.github.maricruzsantos31.gestaoalunos.repository.AlunoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepository;

    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    public Optional<Aluno> buscarPorId(Long id) {
        return alunoRepository.findById(id);
    }
}