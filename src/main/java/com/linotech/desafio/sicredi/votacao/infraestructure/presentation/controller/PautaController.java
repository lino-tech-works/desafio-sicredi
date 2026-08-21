package com.linotech.desafio.sicredi.votacao.infraestructure.presentation.controller;

import com.linotech.desafio.sicredi.votacao.application.PautaService;
import com.linotech.desafio.sicredi.votacao.domain.Pauta;
import com.linotech.desafio.sicredi.votacao.infraestructure.persistence.mapper.PautaMapper;
import com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.request.PautaRequest;
import com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.response.PautaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Pautas", description = "Gerenciamento de pautas")
@RestController
@RequiredArgsConstructor
@RequestMapping("pautas")
public class PautaController {

    private final PautaService pautaService;
    private final PautaMapper mapper;

    @Operation(summary = "Criar pauta", description = "Cria uma nova pauta para votação")
    @ApiResponse(responseCode = "201", description = "Pauta criada com sucesso")
    @PostMapping(produces = "application/vnd.votacao.v1+json")
    @ResponseStatus(HttpStatus.CREATED)
    public PautaResponse criar(@Valid @RequestBody PautaRequest request) {
        log.info("m=criar, s=INIT");
        Pauta pauta = pautaService.criar(request.titulo());
        return mapper.toResponse(pauta);
    }
}