package com.linotech.desafio.sicredi.votacao.infraestructure.persistence.repository;

import com.linotech.desafio.sicredi.votacao.application.client.ElegibilidadeVotoStatus;
import com.linotech.desafio.sicredi.votacao.application.client.VerificadorElegibilidadeVotoClient;
import com.linotech.desafio.sicredi.votacao.application.repository.PautaRepository;
import com.linotech.desafio.sicredi.votacao.application.repository.SessaoVotacaoRepository;
import com.linotech.desafio.sicredi.votacao.application.repository.VotoRepository;
import com.linotech.desafio.sicredi.votacao.domain.Pauta;
import com.linotech.desafio.sicredi.votacao.domain.SessaoVotacao;
import com.linotech.desafio.sicredi.votacao.domain.TipoVoto;
import com.linotech.desafio.sicredi.votacao.domain.Voto;
import com.linotech.desafio.sicredi.votacao.domain.exception.BusinessException;
import com.linotech.desafio.sicredi.votacao.infraestructure.persistence.repository.adapter.RegistraVotoRepositoryAdapter;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class RegistraVotoRepositoryAdapterIT {

    @Autowired
    private RegistraVotoRepositoryAdapter adapter;

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Autowired
    private VotoRepository votoRepository;

    @MockitoBean
    private VerificadorElegibilidadeVotoClient elegibilidadeVotoClient;

    private UUID pautaId;
    private UUID sessaoVotacaoId;
    private final String cpf = "12345678900";

    @BeforeEach
    void setUp() {
        Pauta pauta = pautaRepository.save(new Pauta("Aprovação do orçamento"));
        this.pautaId = pauta.id();

        Instant inicio = Instant.now();
        Instant fim = inicio.plus(10, ChronoUnit.MINUTES);
        SessaoVotacao sessao = sessaoVotacaoRepository.save(new SessaoVotacao(UUID.randomUUID(), pautaId, inicio, fim));
        this.sessaoVotacaoId = sessao.id();
    }

    @Test
    void deveValidarElegibilidadeEPersistirVotoNoH2() {
        when(elegibilidadeVotoClient.verificar(cpf)).thenReturn(ElegibilidadeVotoStatus.ABLE_TO_VOTE);

        Voto votoSalvo = adapter.executar(pautaId, sessaoVotacaoId, cpf, TipoVoto.SIM);

        assertThat(votoSalvo).isNotNull();
        assertThat(votoSalvo.id()).isNotNull();

        boolean existeNoBanco = votoRepository.existsByPautaIdAndCpf(pautaId, cpf);
        assertThat(existeNoBanco).isTrue();

        verify(elegibilidadeVotoClient).verificar(cpf);
    }

    @Test
    void deveLancarExcecaoENaoPersistirQuandoInelegivel() {
        when(elegibilidadeVotoClient.verificar(cpf)).thenReturn(ElegibilidadeVotoStatus.UNABLE_TO_VOTE);

        assertThatThrownBy(() -> adapter.executar(pautaId, sessaoVotacaoId, cpf, TipoVoto.SIM))
                .isInstanceOf(BusinessException.class)
                .hasMessage("associado.nao-elegivel");

        boolean existeNoBanco = votoRepository.existsByPautaIdAndCpf(pautaId, cpf);
        assertThat(existeNoBanco).isFalse();

        verify(elegibilidadeVotoClient).verificar(cpf);
    }
}