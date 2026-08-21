package com.linotech.desafio.sicredi.votacao.application;

import com.linotech.desafio.sicredi.votacao.application.repository.PautaRepository;
import com.linotech.desafio.sicredi.votacao.application.repository.SessaoVotacaoRepository;
import com.linotech.desafio.sicredi.votacao.domain.Pauta;
import com.linotech.desafio.sicredi.votacao.domain.SessaoVotacao;
import com.linotech.desafio.sicredi.votacao.domain.exception.NotFoundException;
import com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.request.SessaoVotacaoRequest;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessaoVotacaoServiceTest {

    private static final UUID PAUTA_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final Instant INICIO = Instant.parse("2026-08-19T23:00:00Z");
    private static final Clock CLOCK = Clock.fixed(INICIO, ZoneOffset.UTC);

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Mock
    private Pauta pauta;

    private SessaoVotacaoService service;

    @BeforeEach
    void setUp() {
        service = new SessaoVotacaoService(pautaRepository, sessaoVotacaoRepository, CLOCK);
    }

    @Test
    void deveAbrirSessaoComDuracaoPadraoQuandoDuracaoNaoForInformada() {
        when(pautaRepository.findById(PAUTA_ID)).thenReturn(Optional.of(pauta));
        when(sessaoVotacaoRepository.save(any(SessaoVotacao.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SessaoVotacao resultado = service.abrir(PAUTA_ID, null);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isNotNull();
        assertThat(resultado.pautaId()).isEqualTo(PAUTA_ID);
        assertThat(resultado.inicio()).isEqualTo(INICIO);
        assertThat(resultado.fim()).isEqualTo(INICIO.plusSeconds(60));

        verify(pautaRepository).findById(PAUTA_ID);
        verify(sessaoVotacaoRepository).save(any(SessaoVotacao.class));

        verifyNoMoreInteractions(pautaRepository, sessaoVotacaoRepository);
    }

    @Test
    void deveAbrirSessaoComDuracaoInformada() {
        Duration duracao = Duration.ofMinutes(5);
        when(pautaRepository.findById(PAUTA_ID)).thenReturn(Optional.of(pauta));
        when(sessaoVotacaoRepository.save(any(SessaoVotacao.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SessaoVotacao resultado = service.abrir(PAUTA_ID, new SessaoVotacaoRequest(duracao.toMinutes()));

        assertThat(resultado).isNotNull();
        assertThat(resultado.pautaId()).isEqualTo(PAUTA_ID);
        assertThat(resultado.inicio()).isEqualTo(INICIO);
        assertThat(resultado.fim()).isEqualTo(INICIO.plus(duracao));

        verify(pautaRepository).findById(PAUTA_ID);
        verify(sessaoVotacaoRepository).save(any(SessaoVotacao.class));

        verifyNoMoreInteractions(pautaRepository, sessaoVotacaoRepository);
    }

    @Test
    void devePersistirSessaoComOsDadosCorretos() {
        Duration duracao = Duration.ofMinutes(10);

        when(pautaRepository.findById(PAUTA_ID)).thenReturn(Optional.of(pauta));

        when(sessaoVotacaoRepository.save(any(SessaoVotacao.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.abrir(PAUTA_ID, new SessaoVotacaoRequest(duracao.toMinutes()));

        ArgumentCaptor<SessaoVotacao> captor =  ArgumentCaptor.forClass(SessaoVotacao.class);

        verify(sessaoVotacaoRepository).save(captor.capture());

        SessaoVotacao sessaoPersistida = captor.getValue();

        assertThat(sessaoPersistida.id()).isNotNull();
        assertThat(sessaoPersistida.pautaId()).isEqualTo(PAUTA_ID);
        assertThat(sessaoPersistida.inicio()).isEqualTo(INICIO);
        assertThat(sessaoPersistida.fim()).isEqualTo(INICIO.plus(Duration.ofMinutes(10)));
    }

    @Test
    void naoDeveAbrirSessaoQuandoPautaNaoExistir() {
        when(pautaRepository.findById(PAUTA_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.abrir(PAUTA_ID, null))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("pauta.nao-encontrada");

        verify(pautaRepository).findById(PAUTA_ID);
        verify(sessaoVotacaoRepository, never()).save(any(SessaoVotacao.class));

        verifyNoMoreInteractions(pautaRepository, sessaoVotacaoRepository);
    }

    @Test
    void naoDevePersistirSessaoQuandoDuracaoForZero() {
        when(pautaRepository.findById(PAUTA_ID)).thenReturn(Optional.of(pauta));

        Duration duracao = Duration.ZERO;
        SessaoVotacaoRequest request = new SessaoVotacaoRequest(duracao.toMinutes());

        assertThatThrownBy(() -> service.abrir(PAUTA_ID, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Fim deve ser posterior ao início");

        verify(pautaRepository).findById(PAUTA_ID);
        verify(sessaoVotacaoRepository, never()).save(any(SessaoVotacao.class));

        verifyNoMoreInteractions(pautaRepository, sessaoVotacaoRepository);
    }


    @Test
    void naoDevePersistirSessaoQuandoDuracaoForNegativa() {
        when(pautaRepository.findById(PAUTA_ID)).thenReturn(Optional.of(pauta));

        Duration duracao = Duration.ofMinutes(-1);
        SessaoVotacaoRequest request = new SessaoVotacaoRequest(duracao.toMinutes());

        assertThatThrownBy(() -> service.abrir(PAUTA_ID, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Fim deve ser posterior ao início");

        verify(pautaRepository).findById(PAUTA_ID);
        verify(sessaoVotacaoRepository, never()).save(any(SessaoVotacao.class));

        verifyNoMoreInteractions(pautaRepository,sessaoVotacaoRepository);
    }

}