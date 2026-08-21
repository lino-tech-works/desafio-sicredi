package com.linotech.desafio.sicredi.votacao.infraestructure.presentation.controller;

import com.linotech.desafio.sicredi.votacao.application.SessaoVotacaoService;
import com.linotech.desafio.sicredi.votacao.domain.SessaoVotacao;
import com.linotech.desafio.sicredi.votacao.infraestructure.persistence.mapper.SessaoVotacaoMapper;
import com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.request.SessaoVotacaoRequest;
import com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.response.SessaoVotacaoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Sessões de Votação", description = "Gerenciamento de sessões de votação")
@RestController
@RequestMapping("pautas")
@RequiredArgsConstructor
public class SessaoVotacaoController {

    private final SessaoVotacaoService service;
    private final SessaoVotacaoMapper mapper;

    @Operation(summary = "Abrir sessão", description = "Abre uma sessão de votação para uma pauta")
    @ApiResponse(responseCode = "201", description = "Sessão aberta com sucesso")
    @PostMapping(value = "/{pautaId}/sessao",
                 produces =  "application/vnd.votacao.v1+json")
    @ResponseStatus(HttpStatus.CREATED)
    public SessaoVotacaoResponse abrir(@PathVariable UUID pautaId,
                                       @RequestBody(required = false) SessaoVotacaoRequest request) {

        log.info("m=abrir, s=INIT, pautaId={}", pautaId);
        SessaoVotacao sessaoVotacao = service.abrir(pautaId, request);
        return mapper.toResponse(sessaoVotacao);
    }
}