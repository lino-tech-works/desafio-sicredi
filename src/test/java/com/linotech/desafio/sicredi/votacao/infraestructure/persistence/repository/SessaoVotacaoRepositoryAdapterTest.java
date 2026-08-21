package com.linotech.desafio.sicredi.votacao.infraestructure.persistence.repository;

import com.linotech.desafio.sicredi.votacao.application.repository.SessaoVotacaoRepository;
import com.linotech.desafio.sicredi.votacao.domain.SessaoVotacao;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class SessaoVotacaoRepositoryAdapterTest {

    @Autowired
    private SessaoVotacaoRepository repository;

    @Test
    void devePersistirESerRecuperada() {

        UUID pautaId = UUID.randomUUID();
        Instant inicio = Instant.parse("2026-08-19T23:00:00Z");
        Instant fim = inicio.plusSeconds(60);

        SessaoVotacao sessao = SessaoVotacao.abrir(pautaId, inicio, fim);
        repository.save(sessao);

        Optional<SessaoVotacao> encontrada = repository.findById(sessao.id());

        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().id()).isEqualTo(sessao.id());
        assertThat(encontrada.get().pautaId()).isEqualTo(pautaId);
        assertThat(encontrada.get().inicio()).isEqualTo(inicio);
        assertThat(encontrada.get().fim()).isEqualTo(fim);
    }
}