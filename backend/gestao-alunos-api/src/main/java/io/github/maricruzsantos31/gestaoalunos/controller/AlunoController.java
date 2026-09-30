package io.github.maricruzsantos31.gestaoalunos.controller;

import io.github.maricruzsantos31.gestaoalunos.dto.AlunoListagemResponse;
import io.github.maricruzsantos31.gestaoalunos.enums.StatusAluno;
import io.github.maricruzsantos31.gestaoalunos.service.AlunoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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