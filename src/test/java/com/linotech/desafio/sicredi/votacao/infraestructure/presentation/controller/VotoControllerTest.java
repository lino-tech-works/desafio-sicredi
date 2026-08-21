package com.linotech.desafio.sicredi.votacao.infraestructure.presentation.controller;

import com.linotech.desafio.sicredi.votacao.application.RegistrarVotoService;
import com.linotech.desafio.sicredi.votacao.application.VotoService;
import com.linotech.desafio.sicredi.votacao.domain.ResultadoVotacao;
import com.linotech.desafio.sicredi.votacao.domain.TipoVoto;
import com.linotech.desafio.sicredi.votacao.domain.Voto;
import com.linotech.desafio.sicredi.votacao.domain.exception.BusinessException;
import com.linotech.desafio.sicredi.votacao.domain.exception.NotFoundException;
import com.linotech.desafio.sicredi.votacao.infraestructure.config.ClockConfig;
import com.linotech.desafio.sicredi.votacao.infraestructure.exception.GlobalExceptionHandler;
import com.linotech.desafio.sicredi.votacao.infraestructure.persistence.mapper.ResultadoVotacaoMapper;
import com.linotech.desafio.sicredi.votacao.infraestructure.persistence.mapper.VotoMapper;
import com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.request.VotoRequest;
import com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.response.ResultadoVotacaoResponse;
import com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.response.VotoResponse;
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
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VotoController.class)
@Import({GlobalExceptionHandler.class, ClockConfig.class})
class VotoControllerTest {

    private static final UUID PAUTA_ID = UUID.randomUUID();
    private static final UUID SESSAO_VOTACAO_ID = UUID.randomUUID();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VotoService service;

    @MockitoBean
    private RegistrarVotoService registrarVotoService;

    @MockitoBean
    private VotoMapper votoMapper;

    @MockitoBean
    private ResultadoVotacaoMapper resultadoVotacaoMapper;

    @Test
    void deveRegistrarVoto() throws Exception {
        VotoRequest request = new VotoRequest("12345678900", TipoVoto.SIM);

        Voto voto = Voto.registrar(PAUTA_ID, SESSAO_VOTACAO_ID, request.cpf(), request.tipo(), Instant.parse("2026-08-20T10:00:00Z"));

        when(registrarVotoService.registrar(PAUTA_ID, SESSAO_VOTACAO_ID, request.cpf(), request.tipo())).thenReturn(voto);
        when(votoMapper.toResponse(voto)).thenReturn(
                new VotoResponse(voto.id(), voto.pautaId(), voto.sessaoVotacaoId(), "***.456.789-**", voto.tipo(), voto.criadoEm())
        );

        mockMvc.perform(
                post("/pautas/{pautaId}/sessao/{sessaoVotacaoId}/votos",
                        PAUTA_ID,
                        SESSAO_VOTACAO_ID
                )
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isCreated())
        .andExpect(content().contentTypeCompatibleWith(MediaType.parseMediaType("application/vnd.votacao.v1+json")))
        .andExpect(jsonPath("$.id").value(voto.id().toString()))
        .andExpect(jsonPath("$.pautaId").value(PAUTA_ID.toString()))
        .andExpect(jsonPath("$.sessaoVotacaoId").value(SESSAO_VOTACAO_ID.toString()))
        .andExpect(jsonPath("$.cpf").value("***.456.789-**"))
        .andExpect(jsonPath("$.tipo").value("SIM"));

        verify(registrarVotoService).registrar(PAUTA_ID, SESSAO_VOTACAO_ID, request.cpf(), request.tipo());
    }

    @Test
    void deveRetornarResultadoDaPauta() throws Exception {
        ResultadoVotacao resultado = new ResultadoVotacao(PAUTA_ID, 5L, 3L);

        when(service.resultado(PAUTA_ID)).thenReturn(resultado);
        when(resultadoVotacaoMapper.toResponse(resultado)).thenReturn(
                new ResultadoVotacaoResponse(PAUTA_ID, 5L, 3L)
        );

        mockMvc.perform(
                get("/pautas/{pautaId}/resultado", PAUTA_ID)
        )
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.parseMediaType("application/vnd.votacao.v1+json")))
        .andExpect(jsonPath("$.pautaId").value(PAUTA_ID.toString()))
        .andExpect(jsonPath("$.quantidadeSim").value(5))
        .andExpect(jsonPath("$.quantidadeNao").value(3));

        verify(service).resultado(PAUTA_ID);
    }

    @Test
    void naoDeveRegistrarVotoComRequestInvalido() throws Exception {
        VotoRequest request = new VotoRequest(null, null);

        mockMvc.perform(
                post("/pautas/{pautaId}/sessao/{sessaoVotacaoId}/votos",
                        PAUTA_ID,
                        SESSAO_VOTACAO_ID
                )
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest());

        verifyNoInteractions(registrarVotoService);
    }

    @Test
    void deveRetornarNotFoundQuandoPautaNaoExiste() throws Exception {
        when(service.resultado(PAUTA_ID))
                .thenThrow(new NotFoundException("pauta.nao-encontrada"));

        mockMvc.perform(
                get("/pautas/{pautaId}/resultado", PAUTA_ID)
        )
        .andExpect(status().isNotFound());

        verify(service).resultado(PAUTA_ID);
    }

    @Test
    void deveRetornarBadRequestQuandoVotoForDuplicado() throws Exception {
        VotoRequest request = new VotoRequest("12345678900", TipoVoto.SIM);

        when(registrarVotoService.registrar(PAUTA_ID, SESSAO_VOTACAO_ID, request.cpf(), request.tipo())).thenThrow(new BusinessException("voto.duplicado"));

        mockMvc.perform(
                post("/pautas/{pautaId}/sessao/{sessaoVotacaoId}/votos",
                        PAUTA_ID,
                        SESSAO_VOTACAO_ID
                )
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest());

        verify(registrarVotoService).registrar(PAUTA_ID, SESSAO_VOTACAO_ID, request.cpf(), request.tipo());
    }

    @Test
    void deveRetornarBadRequestQuandoSessaoEstiverFechada() throws Exception {
        VotoRequest request = new VotoRequest("12345678900", TipoVoto.SIM);

        when(registrarVotoService.registrar(PAUTA_ID, SESSAO_VOTACAO_ID, request.cpf(), request.tipo())).thenThrow(new BusinessException("sessao.fechada"));

        mockMvc.perform(
                post("/pautas/{pautaId}/sessao/{sessaoVotacaoId}/votos",
                        PAUTA_ID,
                        SESSAO_VOTACAO_ID
                )
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest());

        verify(registrarVotoService).registrar(PAUTA_ID, SESSAO_VOTACAO_ID, request.cpf(), request.tipo());
    }
}
