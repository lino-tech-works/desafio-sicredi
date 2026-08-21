package com.linotech.desafio.sicredi.votacao.infraestructure.presentation.controller;

import com.linotech.desafio.sicredi.votacao.application.PautaService;
import com.linotech.desafio.sicredi.votacao.domain.Pauta;
import com.linotech.desafio.sicredi.votacao.infraestructure.config.ClockConfig;
import com.linotech.desafio.sicredi.votacao.infraestructure.exception.GlobalExceptionHandler;
import com.linotech.desafio.sicredi.votacao.infraestructure.persistence.mapper.PautaMapper;
import com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.response.PautaResponse;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PautaController.class)
@Import({GlobalExceptionHandler.class, ClockConfig.class})
class PautaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PautaService pautaService;

    @MockitoBean
    private PautaMapper mapper;

    @Test
    void deveCriarUmaPauta() throws Exception {

        UUID pautaId = UUID.randomUUID();
        Pauta pauta = new Pauta(pautaId, "Aprovação do orçamento");

        when(pautaService.criar("Aprovação do orçamento")).thenReturn(pauta);
        when(mapper.toResponse(pauta)).thenReturn(new PautaResponse(pautaId, "Aprovação do orçamento"));

        mockMvc.perform(
                post("/pautas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                                {
                                  "titulo": "Aprovação do orçamento"
                                }
                             """)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.parseMediaType("application/vnd.votacao.v1+json")))
                .andExpect(jsonPath("$.id").value(pautaId.toString()))
                .andExpect(jsonPath("$.titulo").value("Aprovação do orçamento"));
    }

    @Test
    void naoDeveCriarPautaSemTitulo() throws Exception {

        mockMvc.perform(
                post("/pautas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                                {
                                  "titulo": ""
                                }
                            """)
                )
                .andExpect(status().isBadRequest());
    }
}