package io.github.maricruzsantos31.gestaoalunos.controller;

import io.github.maricruzsantos31.gestaoalunos.dto.AlunoAtualizacaoRequest;
import io.github.maricruzsantos31.gestaoalunos.dto.AlunoCadastroRequest;
import io.github.maricruzsantos31.gestaoalunos.dto.AlunoListagemResponse;
import io.github.maricruzsantos31.gestaoalunos.entity.Aluno;
import io.github.maricruzsantos31.gestaoalunos.enums.StatusAluno;
import io.github.maricruzsantos31.gestaoalunos.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @GetMapping
    public Page<AlunoListagemResponse> listar(
            @RequestParam(required = false) String busca,
            @RequestParam(defaultValue = "ATIVO") String status,
            @PageableDefault(
                    size = 10,
                    sort = "nome",
                    direction = Sort.Direction.ASC
            ) Pageable pageable) {

        StatusAluno statusFiltro = converterStatus(status);

        return alunoService.listar(
                busca,
                statusFiltro,
                pageable
        );
    }

    @GetMapping("/{id}")
    public Aluno buscarPorId(@PathVariable Long id) {

        return alunoService.buscarPorId(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Aluno não encontrado."
                        )
                );
    }

    @GetMapping("/cpf/{cpf}")
    public boolean verificarCpf(@PathVariable String cpf) {
        return alunoService.verificarCpf(cpf);
    }

    @GetMapping("/email/{email}")
    public boolean verificarEmail(@PathVariable String email) {
        return alunoService.verificarEmail(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlunoListagemResponse cadastrar(
            @Valid @RequestBody AlunoCadastroRequest request) {

        Aluno aluno = alunoService.cadastrar(
                request.getNome(),
                request.getEmail(),
                request.getCpf(),
                request.getTelefone(),
                request.getFoto(),
                request.getFotoContentType()
        );

        return new AlunoListagemResponse(aluno);
    }

    @PutMapping("/{id}")
    public AlunoListagemResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AlunoAtualizacaoRequest request) {

        Aluno aluno = alunoService.atualizar(
                id,
                request.getNome(),
                request.getEmail(),
                request.getTelefone(),
                request.getFoto(),
                request.getFotoContentType(),
                request.getStatus()
        );

        return new AlunoListagemResponse(aluno);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        alunoService.excluir(id);
    }

    private StatusAluno converterStatus(String status) {

        if (status == null
                || status.isBlank()
                || status.equalsIgnoreCase("TODOS")) {

            return null;
        }

        try {
            return StatusAluno.valueOf(
                    status.trim().toUpperCase()
            );
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Status deve ser ATIVO, INATIVO ou TODOS."
            );
        }
    }
}