package com.linotech.desafio.sicredi.votacao.infraestructure.presentation.controller;

import com.linotech.desafio.sicredi.votacao.application.RegistrarVotoService;
import com.linotech.desafio.sicredi.votacao.application.VotoService;
import com.linotech.desafio.sicredi.votacao.domain.ResultadoVotacao;
import com.linotech.desafio.sicredi.votacao.domain.Voto;
import com.linotech.desafio.sicredi.votacao.infraestructure.persistence.mapper.ResultadoVotacaoMapper;
import com.linotech.desafio.sicredi.votacao.infraestructure.persistence.mapper.VotoMapper;
import com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.request.VotoRequest;
import com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.response.ResultadoVotacaoResponse;
import com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.response.VotoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Votos", description = "Registro de votos e consulta de resultados")
@RestController
@RequestMapping("pautas")
@RequiredArgsConstructor
public class VotoController {

    private final VotoService service;
    private final RegistrarVotoService registrarVotoService;
    private final VotoMapper votoMapper;
    private final ResultadoVotacaoMapper resultadoVotacaoMapper;

    @Operation(summary = "Registrar voto", description = "Registra o voto de um associado em uma sessão de votação")
    @ApiResponse(responseCode = "201", description = "Voto registrado com sucesso")
    @PostMapping(
                value = "/{pautaId}/sessao/{sessaoVotacaoId}/votos",
                produces =  "application/vnd.votacao.v1+json")
    @ResponseStatus(HttpStatus.CREATED)
    public VotoResponse votar(@PathVariable UUID pautaId,
                              @PathVariable UUID sessaoVotacaoId,
                              @Valid @RequestBody VotoRequest request) {
        log.info("m=votar, s=INIT, pautaId={}, sessaoVotacaoId={}", pautaId, sessaoVotacaoId);
        Voto voto = registrarVotoService.registrar(pautaId, sessaoVotacaoId, request.cpf(), request.tipo());
        return votoMapper.toResponse(voto);
    }

    @Operation(summary = "Consultar resultado", description = "Retorna o resultado da votação de uma pauta")
    @ApiResponse(responseCode = "200", description = "Resultado retornado com sucesso")
    @GetMapping(
            value = "/{pautaId}/resultado",
            produces =  "application/vnd.votacao.v1+json")
    public ResultadoVotacaoResponse resultado(@PathVariable UUID pautaId) {
        log.info("m=resultado, s=INIT, pautaId={}", pautaId);
        ResultadoVotacao resultado = service.resultado(pautaId);
        return resultadoVotacaoMapper.toResponse(resultado);
    }
}