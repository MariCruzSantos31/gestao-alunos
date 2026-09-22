package io.github.maricruzsantos31.gestaoalunos.repository;

import io.github.maricruzsantos31.gestaoalunos.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
}
