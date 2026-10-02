package io.github.maricruzsantos31.gestaoalunos.service;

import io.github.maricruzsantos31.gestaoalunos.dto.AlunoListagemResponse;
import io.github.maricruzsantos31.gestaoalunos.entity.Aluno;
import io.github.maricruzsantos31.gestaoalunos.enums.StatusAluno;
import io.github.maricruzsantos31.gestaoalunos.repository.AlunoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Year;
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

        String buscaNormalizada = normalizarBusca(busca);

        Page<Aluno> alunos;

        if (status == null) {
            alunos = alunoRepository.buscarTodos(
                    buscaNormalizada,
                    pageable
            );
        } else if (status == StatusAluno.INATIVO) {
            alunos = alunoRepository.buscarInativos(
                    buscaNormalizada,
                    pageable
            );
        } else {
            alunos = alunoRepository.buscarAtivos(
                    buscaNormalizada,
                    pageable
            );
        }

        return alunos.map(AlunoListagemResponse::new);
    }

    public long contar() {
        return alunoRepository.count();
    }

    public Aluno salvar(Aluno aluno) {
        return alunoRepository.save(aluno);
    }

    public boolean verificarCpf(String cpf) {
        return alunoRepository.existsByCpf(cpf);
    }

    public boolean verificarEmail(String email) {
        return alunoRepository.existsByEmailAndExcluidoFalse(email);
    }

    public Aluno cadastrar(
            String nome,
            String email,
            String cpf,
            String telefone,
            byte[] foto,
            String fotoContentType) {

        if (!isCpfValido(cpf)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "CPF inválido."
            );
        }

        if (alunoRepository.existsByCpf(cpf)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "CPF já cadastrado."
            );
        }

        if (alunoRepository.existsByEmailAndExcluidoFalse(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "E-mail já cadastrado."
            );
        }

        if (!isTelefoneValido(telefone)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Telefone deve conter DDD e número."
            );
        }

        Aluno aluno = new Aluno(
                gerarMatricula(),
                nome,
                email,
                cpf,
                telefone
        );

        aluno.setFoto(foto);
        aluno.setFotoContentType(fotoContentType);

        return alunoRepository.save(aluno);
    }

    public Aluno atualizar(
            Long id,
            String nome,
            String email,
            String telefone,
            byte[] foto,
            String fotoContentType,
            StatusAluno status) {

        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Aluno não encontrado."
                        )
                );

        if (alunoRepository.existsByEmailAndIdNotAndExcluidoFalse(
                email,
                id)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "E-mail já cadastrado."
            );
        }

        if (!isTelefoneValido(telefone)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Telefone deve conter DDD e número."
            );
        }

        aluno.setNome(nome);
        aluno.setEmail(email);
        aluno.setTelefone(telefone);
        aluno.setFoto(foto);
        aluno.setFotoContentType(fotoContentType);

        if (status != null) {
            aluno.setStatus(status);

            if (status == StatusAluno.ATIVO) {
                aluno.setExcluido(false);
            }
        }

        return alunoRepository.save(aluno);
    }

    public void excluir(Long id) {

        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Aluno não encontrado."
                        )
                );

        aluno.setStatus(StatusAluno.INATIVO);
        aluno.setExcluido(true);

        alunoRepository.save(aluno);
    }

    private String normalizarBusca(String busca) {
        return busca == null || busca.isBlank()
                ? null
                : busca.trim();
    }

    private boolean isCpfValido(String cpf) {

        if (cpf == null || !cpf.matches("\\d{11}")) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int soma = 0;

        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i))
                    * (10 - i);
        }

        int resto = soma % 11;
        int primeiroDigito = resto < 2 ? 0 : 11 - resto;

        if (primeiroDigito !=
                Character.getNumericValue(cpf.charAt(9))) {
            return false;
        }

        soma = 0;

        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i))
                    * (11 - i);
        }

        resto = soma % 11;
        int segundoDigito = resto < 2 ? 0 : 11 - resto;

        return segundoDigito ==
                Character.getNumericValue(cpf.charAt(10));
    }

    private boolean isTelefoneValido(String telefone) {

        if (telefone == null || !telefone.matches("\\d{10,11}")) {
            return false;
        }

        int ddd = Integer.parseInt(telefone.substring(0, 2));

        return ddd >= 11 && ddd <= 99;
    }

    private String gerarMatricula() {

        int ano = Year.now().getValue();
        String prefixo = String.valueOf(ano);

        Optional<Aluno> ultimoAluno =
                alunoRepository
                        .findTopByMatriculaStartingWithOrderByMatriculaDesc(
                                prefixo
                        );

        int proximoNumero = ultimoAluno
                .map(aluno -> Integer.parseInt(
                        aluno.getMatricula().substring(prefixo.length())
                ) + 1)
                .orElse(1);

        return String.format(
                "%s%04d",
                prefixo,
                proximoNumero
        );
    }
}