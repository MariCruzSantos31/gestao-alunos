package io.github.maricruzsantos31.gestaoalunos.service;

import io.github.maricruzsantos31.gestaoalunos.dto.AlunoListagemResponse;
import io.github.maricruzsantos31.gestaoalunos.entity.Aluno;
import io.github.maricruzsantos31.gestaoalunos.enums.StatusAluno;
import io.github.maricruzsantos31.gestaoalunos.repository.AlunoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<AlunoListagemResponse> listar(Pageable pageable) {
        return listar(null, StatusAluno.ATIVO, pageable);
    }

    public Page<AlunoListagemResponse> listar(
            String busca,
            StatusAluno status,
            Pageable pageable) {

        String buscaNormalizada =
                (busca == null || busca.isBlank())
                        ? null
                        : busca.trim();

        return alunoRepository
                .buscar(buscaNormalizada, status, pageable)
                .map(AlunoListagemResponse::new);
    }

    public long contar() {
        return alunoRepository.count();
    }

    public Aluno salvar(Aluno aluno) {
        return alunoRepository.save(aluno);
    }
}