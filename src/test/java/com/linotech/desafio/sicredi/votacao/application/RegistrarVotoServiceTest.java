package com.linotech.desafio.sicredi.votacao.application;

import com.linotech.desafio.sicredi.votacao.application.repository.RegistraVotoRepository;
import com.linotech.desafio.sicredi.votacao.domain.TipoVoto;
import com.linotech.desafio.sicredi.votacao.domain.Voto;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarVotoServiceTest {

    @Mock
    private RegistraVotoRepository repository;

    @InjectMocks
    private RegistrarVotoService service;

    @Test
    void deveRegistrarVotoComSucesso() {
        UUID pautaId = UUID.randomUUID();
        UUID sessaoVotacaoId = UUID.randomUUID();
        String cpf = "12345678900";
        TipoVoto tipo = TipoVoto.SIM;
        Voto votoEsperado = mock(Voto.class);

        when(repository.executar(pautaId, sessaoVotacaoId, cpf, tipo)).thenReturn(votoEsperado);

        Voto resultado = service.registrar(pautaId, sessaoVotacaoId, cpf, tipo);

        assertNotNull(resultado);
        assertEquals(votoEsperado, resultado);
        verify(repository).executar(pautaId, sessaoVotacaoId, cpf, tipo);
    }
}