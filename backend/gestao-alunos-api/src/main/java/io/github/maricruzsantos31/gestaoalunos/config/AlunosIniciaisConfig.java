package io.github.maricruzsantos31.gestaoalunos.config;

import io.github.maricruzsantos31.gestaoalunos.entity.Aluno;
import io.github.maricruzsantos31.gestaoalunos.service.AlunoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AlunosIniciaisConfig {

    @Bean
    public CommandLineRunner carregarAlunosIniciais(AlunoService alunoService) {
        return args -> {

            if (alunoService.contar() == 0) {

                List<Aluno> alunos = List.of(
                        new Aluno("20260001", "Ana Beatriz Silva", "ana.silva@email.com", "10000000108", "81990000001"),
                        new Aluno("20260002", "Bruno Henrique Lima", "bruno.lima@email.com", "10000000280", "81990000002"),
                        new Aluno("20260003", "Camila Rodrigues", "camila.rodrigues@email.com", "10000000361", "81990000003"),
                        new Aluno("20260004", "Daniel Oliveira", "daniel.oliveira@email.com", "10000000442", "81990000004"),
                        new Aluno("20260005", "Eduarda Santos", "eduarda.santos@email.com", "10000000523", "81990000005"),
                        new Aluno("20260006", "Felipe Almeida", "felipe.almeida@email.com", "10000000604", "81990000006"),
                        new Aluno("20260007", "Gabriela Souza", "gabriela.souza@email.com", "10000000795", "81990000007"),
                        new Aluno("20260008", "Henrique Costa", "henrique.costa@email.com", "10000000876", "81990000008"),
                        new Aluno("20260009", "Isabela Ferreira", "isabela.ferreira@email.com", "10000000957", "81990000009"),
                        new Aluno("20260010", "João Pedro Melo", "joao.melo@email.com", "10000001090", "81990000010"),
                        new Aluno("20260011", "Karen Martins", "karen.martins@email.com", "10000001171", "81990000011"),
                        new Aluno("20260012", "Lucas Barbosa", "lucas.barbosa@email.com", "10000001252", "81990000012"),
                        new Aluno("20260013", "Mariana Costa", "mariana.costa@email.com", "10000001333", "81990000013"),
                        new Aluno("20260014", "Nicolas Ribeiro", "nicolas.ribeiro@email.com", "10000001414", "81990000014"),
                        new Aluno("20260015", "Olívia Fernandes", "olivia.fernandes@email.com", "10000001503", "81990000015"),
                        new Aluno("20260016", "Paulo Henrique", "paulo.henrique@email.com", "10000001686", "81990000016"),
                        new Aluno("20260017", "Queila Moura", "queila.moura@email.com", "10000001767", "81990000017"),
                        new Aluno("20260018", "Rafael Gomes", "rafael.gomes@email.com", "10000001848", "81990000018"),
                        new Aluno("20260019", "Sofia Carvalho", "sofia.carvalho@email.com", "10000001929", "81990000019"),
                        new Aluno("20260020", "Thiago Rocha", "thiago.rocha@email.com", "10000002062", "81990000020"),
                        new Aluno("20260021", "Úrsula Mendes", "ursula.mendes@email.com", "10000002143", "81990000021"),
                        new Aluno("20260022", "Victor Nunes", "victor.nunes@email.com", "10000002224", "81990000022"),
                        new Aluno("20260023", "Wesley Araújo", "wesley.araujo@email.com", "10000002305", "81990000023"),
                        new Aluno("20260024", "Yasmin Teixeira", "yasmin.teixeira@email.com", "10000002496", "81990000024"),
                        new Aluno("20260025", "Zeca Monteiro", "zeca.monteiro@email.com", "10000002577", "81990000025")
                );

                alunos.forEach(alunoService::salvar);
            }
        };
    }
}