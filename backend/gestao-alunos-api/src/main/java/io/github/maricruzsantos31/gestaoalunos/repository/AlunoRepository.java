package io.github.maricruzsantos31.gestaoalunos.repository;

import io.github.maricruzsantos31.gestaoalunos.entity.Aluno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    @Query("""
            SELECT a
            FROM Aluno a
            WHERE a.excluido = false
              AND a.status = io.github.maricruzsantos31.gestaoalunos.enums.StatusAluno.ATIVO
              AND (
                    :busca IS NULL
                    OR :busca = ''
                    OR LOWER(a.nome) LIKE CONCAT('%', LOWER(:busca), '%')
                    OR a.matricula LIKE CONCAT(:busca, '%')
                  )
            """)
    Page<Aluno> buscarAtivos(
            @Param("busca") String busca,
            Pageable pageable
    );

    @Query("""
            SELECT a
            FROM Aluno a
            WHERE a.status = io.github.maricruzsantos31.gestaoalunos.enums.StatusAluno.INATIVO
              AND (
                    :busca IS NULL
                    OR :busca = ''
                    OR LOWER(a.nome) LIKE CONCAT('%', LOWER(:busca), '%')
                    OR a.matricula LIKE CONCAT(:busca, '%')
                  )
            """)
    Page<Aluno> buscarInativos(
            @Param("busca") String busca,
            Pageable pageable
    );

    @Query("""
            SELECT a
            FROM Aluno a
            WHERE (
                    :busca IS NULL
                    OR :busca = ''
                    OR LOWER(a.nome) LIKE CONCAT('%', LOWER(:busca), '%')
                    OR a.matricula LIKE CONCAT(:busca, '%')
                  )
            """)
    Page<Aluno> buscarTodos(
            @Param("busca") String busca,
            Pageable pageable
    );

    boolean existsByCpf(String cpf);

    boolean existsByEmailAndExcluidoFalse(String email);

    boolean existsByEmailAndIdNotAndExcluidoFalse(
            String email,
            Long id
    );

    Optional<Aluno> findTopByMatriculaStartingWithOrderByMatriculaDesc(
            String prefixo
    );
}