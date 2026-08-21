package com.linotech.desafio.sicredi.votacao.application;

import com.linotech.desafio.sicredi.votacao.application.repository.PautaRepository;
import com.linotech.desafio.sicredi.votacao.application.repository.SessaoVotacaoRepository;
import com.linotech.desafio.sicredi.votacao.application.repository.VotoRepository;
import com.linotech.desafio.sicredi.votacao.domain.Pauta;
import com.linotech.desafio.sicredi.votacao.domain.ResultadoVotacao;
import com.linotech.desafio.sicredi.votacao.domain.SessaoVotacao;
import com.linotech.desafio.sicredi.votacao.domain.TipoVoto;
import com.linotech.desafio.sicredi.votacao.domain.Voto;
import com.linotech.desafio.sicredi.votacao.domain.exception.BusinessException;
import com.linotech.desafio.sicredi.votacao.domain.exception.NotFoundException;
import com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.request.VotoRequest;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

    private static final String CPF = "12345678900";
    private static final UUID PAUTA_ID = UUID.randomUUID();
    private static final UUID SESSAO_ID = UUID.randomUUID();

    @Mock
    private PautaRepository pautaRepository;

    @Mock
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Mock
    private VotoRepository votoRepository;

    private VotoService service;

    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(Instant.parse("2026-08-20T10:00:00Z"), ZoneOffset.UTC);

        service = new VotoService(clock,votoRepository,  pautaRepository, sessaoVotacaoRepository);
    }

    @Test
    void deveRegistrarVotoSim() {
        Pauta pauta = Pauta.criar(PAUTA_ID,"Pauta teste");

        SessaoVotacao sessao = SessaoVotacao.abrir(PAUTA_ID, Instant.parse("2026-08-20T09:59:00Z"),
                Instant.parse("2026-08-20T10:01:00Z")
        );

        VotoRequest request = new VotoRequest(CPF, TipoVoto.SIM);

        when(pautaRepository.findById(PAUTA_ID)).thenReturn(Optional.of(pauta));
        when(sessaoVotacaoRepository.findById(SESSAO_ID)).thenReturn(Optional.of(sessao));
        when(votoRepository.existsByPautaIdAndCpf(PAUTA_ID, CPF)).thenReturn(false);

        Voto votoSalvo = Voto.registrar(PAUTA_ID, SESSAO_ID, CPF, TipoVoto.SIM, Instant.parse("2026-08-20T10:00:00Z"));

        when(votoRepository.salvar(any(Voto.class))).thenReturn(votoSalvo);

        Voto resultado = service.votar(PAUTA_ID, SESSAO_ID, request.cpf(), request.tipo());

        assertThat(resultado).isNotNull();
        verify(votoRepository).salvar(any(Voto.class));
    }

    @Test
    void deveRegistrarVotoNao() {
        Pauta pauta = Pauta.criar(PAUTA_ID,"Pauta teste");

        SessaoVotacao sessao = SessaoVotacao.abrir(PAUTA_ID, Instant.parse("2026-08-20T09:59:00Z"),
                Instant.parse("2026-08-20T10:01:00Z")
        );

        VotoRequest request = new VotoRequest(CPF, TipoVoto.NAO);

        when(pautaRepository.findById(PAUTA_ID)).thenReturn(Optional.of(pauta));
        when(sessaoVotacaoRepository.findById(SESSAO_ID)).thenReturn(Optional.of(sessao));
        when(votoRepository.existsByPautaIdAndCpf(PAUTA_ID, CPF)).thenReturn(false);

        Voto votoSalvo = Voto.registrar(PAUTA_ID, SESSAO_ID, CPF, TipoVoto.NAO, Instant.parse("2026-08-20T10:00:00Z"));

        when(votoRepository.salvar(any(Voto.class))).thenReturn(votoSalvo);

        Voto resultado = service.votar(PAUTA_ID, SESSAO_ID, request.cpf(), request.tipo());

        assertThat(resultado).isNotNull();
        verify(votoRepository).salvar(any(Voto.class));
    }

    @Test
    void naoDeveVotarQuandoPautaNaoExiste() {
        when(pautaRepository.findById(PAUTA_ID)).thenReturn(Optional.empty());

        VotoRequest request = new VotoRequest("12345678900", TipoVoto.SIM);

        String cpf = request.cpf();
        TipoVoto tipo = request.tipo();

        assertThatThrownBy(() -> service.votar(PAUTA_ID, SESSAO_ID, cpf, tipo))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("pauta.nao-encontrada");

        verifyNoInteractions(sessaoVotacaoRepository);
        verifyNoInteractions(votoRepository);
    }

    @Test
    void naoDeveVotarQuandoSessaoNaoExiste() {
        Pauta pauta = Pauta.criar(PAUTA_ID,"Pauta teste");

        when(pautaRepository.findById(PAUTA_ID)).thenReturn(Optional.of(pauta));
        when(sessaoVotacaoRepository.findById(SESSAO_ID)).thenReturn(Optional.empty());

        VotoRequest request = new VotoRequest("12345678900", TipoVoto.SIM);

        String cpf = request.cpf();
        TipoVoto tipo = request.tipo();

        assertThatThrownBy(() -> service.votar(PAUTA_ID, SESSAO_ID, cpf, tipo))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("sessao.nao-encontrada");

        verifyNoInteractions(votoRepository);
    }

    @Test
    void naoDeveVotarQuandoSessaoNaoPertenceAPauta() {
        Pauta pauta = Pauta.criar(PAUTA_ID, "Pauta teste");

        UUID outraPautaId = UUID.randomUUID();
        SessaoVotacao sessao = SessaoVotacao.abrir(outraPautaId, Instant.parse("2026-08-20T09:59:00Z"),
                Instant.parse("2026-08-20T10:01:00Z")
        );

        when(pautaRepository.findById(PAUTA_ID)).thenReturn(Optional.of(pauta));
        when(sessaoVotacaoRepository.findById(SESSAO_ID)).thenReturn(Optional.of(sessao));

        VotoRequest request = new VotoRequest("12345678900", TipoVoto.SIM);

        String cpf = request.cpf();
        TipoVoto tipo = request.tipo();

        assertThatThrownBy(() -> service.votar(PAUTA_ID, SESSAO_ID, cpf, tipo))
                .isInstanceOf(BusinessException.class)
                .hasMessage("sessao.nao-pertence-pauta");

        verifyNoInteractions(votoRepository);
    }

    @Test
    void naoDeveVotarQuandoSessaoEstaFechada() {
        Pauta pauta = Pauta.criar(PAUTA_ID,"Pauta teste");

        SessaoVotacao sessao = SessaoVotacao.abrir(PAUTA_ID, Instant.parse("2026-08-20T09:00:00Z"),
                Instant.parse("2026-08-20T09:59:00Z")
        );

        when(pautaRepository.findById(PAUTA_ID)).thenReturn(Optional.of(pauta));
        when(sessaoVotacaoRepository.findById(SESSAO_ID)).thenReturn(Optional.of(sessao));

        VotoRequest request = new VotoRequest("12345678900", TipoVoto.SIM);

        String cpf = request.cpf();
        TipoVoto tipo = request.tipo();

        assertThatThrownBy(() -> service.votar(PAUTA_ID, SESSAO_ID, cpf, tipo))
                .isInstanceOf(BusinessException.class)
                .hasMessage("sessao.fechada");

        verifyNoInteractions(votoRepository);
    }

    @Test
    void naoDevePermitirDoisVotosDoMesmoAssociadoNaMesmaPauta() {
        Pauta pauta = Pauta.criar(PAUTA_ID,"Pauta teste");

        SessaoVotacao sessao = SessaoVotacao.abrir(PAUTA_ID, Instant.parse("2026-08-20T09:59:00Z"),
                Instant.parse("2026-08-20T10:01:00Z")
        );

        when(pautaRepository.findById(PAUTA_ID)).thenReturn(Optional.of(pauta));
        when(sessaoVotacaoRepository.findById(SESSAO_ID)).thenReturn(Optional.of(sessao));
        when(votoRepository.existsByPautaIdAndCpf(PAUTA_ID, CPF)).thenReturn(true);

        VotoRequest request = new VotoRequest(CPF, TipoVoto.NAO);

        String cpf = request.cpf();
        TipoVoto tipo = request.tipo();

        assertThatThrownBy(() -> service.votar(PAUTA_ID, SESSAO_ID, cpf, tipo))
                .isInstanceOf(BusinessException.class).hasMessage("voto.duplicado");

        verify(votoRepository, never()).salvar(any());
    }

    @Test
    void deveRetornarResultadoZeradoQuandoNaoExistemVotos() {
        when(pautaRepository.findById(PAUTA_ID)).thenReturn(Optional.of(Pauta.criar(PAUTA_ID,"Pauta teste")));

        when(votoRepository.contarPorPautaId(PAUTA_ID)).thenReturn(new ResultadoVotacao(PAUTA_ID,0L, 0L));

        ResultadoVotacao resultado = service.resultado(PAUTA_ID);

        assertThat(resultado).isNotNull();
        assertThat(resultado.pautaId()).isEqualTo(PAUTA_ID);
        assertThat(resultado.quantidadeSim()).isZero();
        assertThat(resultado.quantidadeNao()).isZero();
    }

    @Test
    void deveContabilizarVotosSimENao() {
        when(pautaRepository.findById(PAUTA_ID)).thenReturn(Optional.of(Pauta.criar(PAUTA_ID,"Pauta teste")));

        when(votoRepository.contarPorPautaId(PAUTA_ID)).thenReturn(new ResultadoVotacao(PAUTA_ID,3L, 2L));

        ResultadoVotacao resultado = service.resultado(PAUTA_ID);

        assertThat(resultado.pautaId()).isEqualTo(PAUTA_ID);
        assertThat(resultado.quantidadeSim()).isEqualTo(3);
        assertThat(resultado.quantidadeNao()).isEqualTo(2);
    }

    @Test
    void naoDeveRetornarResultadoQuandoPautaNaoExiste() {
        when(pautaRepository.findById(PAUTA_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.resultado(PAUTA_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("pauta.nao-encontrada");

        verifyNoInteractions(votoRepository);
    }
}