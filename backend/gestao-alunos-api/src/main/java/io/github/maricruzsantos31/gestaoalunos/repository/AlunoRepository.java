package io.github.maricruzsantos31.gestaoalunos.repository;

import io.github.maricruzsantos31.gestaoalunos.entity.Aluno;
import io.github.maricruzsantos31.gestaoalunos.enums.StatusAluno;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    @Query("""
            SELECT a
            FROM Aluno a
            WHERE a.excluido = false
              AND (:status IS NULL OR a.status = :status)
              AND (
                    :busca IS NULL
                    OR :busca = ''
                    OR LOWER(a.nome) LIKE CONCAT('%', LOWER(:busca), '%')
                    OR a.matricula LIKE CONCAT(:busca, '%')
                  )
            """)
    Page<Aluno> buscar(
            @Param("busca") String busca,
            @Param("status") StatusAluno status,
            Pageable pageable
    );
}