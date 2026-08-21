package com.linotech.desafio.sicredi.votacao.infraestructure.presentation.controller;

import com.linotech.desafio.sicredi.votacao.application.SessaoVotacaoService;
import com.linotech.desafio.sicredi.votacao.domain.SessaoVotacao;
import com.linotech.desafio.sicredi.votacao.domain.exception.BusinessException;
import com.linotech.desafio.sicredi.votacao.domain.exception.NotFoundException;
import com.linotech.desafio.sicredi.votacao.infraestructure.config.ClockConfig;
import com.linotech.desafio.sicredi.votacao.infraestructure.exception.GlobalExceptionHandler;
import com.linotech.desafio.sicredi.votacao.infraestructure.persistence.mapper.SessaoVotacaoMapper;
import com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.request.SessaoVotacaoRequest;
import com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.response.SessaoVotacaoResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessaoVotacaoController.class)
@Import({GlobalExceptionHandler.class, ClockConfig.class})
class SessaoVotacaoControllerTest {

    private static final UUID PAUTA_ID = UUID.randomUUID();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SessaoVotacaoService service;

    @MockitoBean
    private SessaoVotacaoMapper mapper;

    @Test
    void deveAbrirSessao() throws Exception {
        SessaoVotacaoRequest request = new SessaoVotacaoRequest(Duration.ofMinutes(5).toMinutes());

        SessaoVotacao response =  SessaoVotacao.abrir(
                PAUTA_ID,
                Instant.parse("2026-08-20T10:00:00Z"),
                Instant.parse("2026-08-20T10:05:00Z")
        );

        when(service.abrir(PAUTA_ID, request)).thenReturn(response);
        when(mapper.toResponse(response)).thenReturn(
                new SessaoVotacaoResponse(response.id(), response.pautaId(), response.inicio(), response.fim())
        );

        mockMvc.perform(
                post("/pautas/{pautaId}/sessao", PAUTA_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isCreated())
        .andExpect(content().contentTypeCompatibleWith(MediaType.parseMediaType("application/vnd.votacao.v1+json")))
        .andExpect(jsonPath("$.id").value(response.id().toString()))
        .andExpect(jsonPath("$.pautaId").value(PAUTA_ID.toString()))
        .andExpect(jsonPath("$.inicio").value(response.inicio().toString()))
        .andExpect(jsonPath("$.fim").value(response.fim().toString()));

        verify(service).abrir(PAUTA_ID, request);
    }

    @Test
    void deveAbrirSessaoComRequestNulo() throws Exception {
        SessaoVotacao response = SessaoVotacao.abrir(PAUTA_ID,
                Instant.parse("2026-08-20T10:00:00Z"),
                Instant.parse("2026-08-20T10:01:00Z")
        );

        when(service.abrir(PAUTA_ID, null)).thenReturn(response);
        when(mapper.toResponse(response)).thenReturn(
                new SessaoVotacaoResponse(response.id(), response.pautaId(), response.inicio(), response.fim())
        );

        mockMvc.perform(
                post("/pautas/{pautaId}/sessao", PAUTA_ID)
        )
        .andExpect(status().isCreated())
        .andExpect(content().contentTypeCompatibleWith(MediaType.parseMediaType("application/vnd.votacao.v1+json")))
        .andExpect(jsonPath("$.id").value(response.id().toString()))
        .andExpect(jsonPath("$.pautaId").value(PAUTA_ID.toString()))
        .andExpect(jsonPath("$.inicio").value(response.inicio().toString()))
        .andExpect(jsonPath("$.fim").value(response.fim().toString()));

        verify(service).abrir(PAUTA_ID, null);
    }

    @Test
    void deveRetornarNotFoundQuandoPautaNaoExiste() throws Exception {
        when(service.abrir(PAUTA_ID, null)).thenThrow(new NotFoundException("pauta.nao-encontrada"));

        mockMvc.perform(
                post("/pautas/{pautaId}/sessao", PAUTA_ID)
        )
        .andExpect(status().isNotFound());

        verify(service).abrir(PAUTA_ID, null);
    }

    @Test
    void deveRetornarBadRequestQuandoDuracaoForInvalida() throws Exception {
        SessaoVotacaoRequest request = new SessaoVotacaoRequest(Duration.ZERO.toMinutes());

        when(service.abrir(PAUTA_ID, request)).thenThrow(new BusinessException("sessao.duracao.invalida"));

        mockMvc.perform(
                post("/pautas/{pautaId}/sessao", PAUTA_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest());

        verify(service).abrir(PAUTA_ID, request);
    }
}