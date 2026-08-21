package com.linotech.desafio.sicredi.votacao.domain;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SessaoVotacaoTest {

    private static final UUID PAUTA_ID = UUID.randomUUID();

    @Test
    void deveEstarAbertaDuranteOPeriodoDeVotacao() {
        Instant inicio = Instant.parse("2026-08-19T23:00:00Z");
        Instant fim = inicio.plusSeconds(60);

        SessaoVotacao sessao = SessaoVotacao.abrir(UUID.randomUUID(), inicio, fim);

        Instant agora = inicio.plusSeconds(30);

        assertThat(sessao.estaAberta(agora)).isTrue();
    }

    @Test
    void naoDeveEstarAbertaAntesDoInicio() {
        Instant inicio = Instant.parse("2026-08-19T23:00:00Z");
        Instant fim = inicio.plusSeconds(60);

        SessaoVotacao sessao = SessaoVotacao.abrir(UUID.randomUUID(), inicio, fim);

        Instant agora = inicio.minusSeconds(1);

        assertThat(sessao.estaAberta(agora)).isFalse();
    }

    @Test
    void naoDeveEstarAbertaDepoisDoFim() {
        Instant inicio = Instant.parse("2026-08-19T23:00:00Z");
        Instant fim = inicio.plusSeconds(60);

        SessaoVotacao sessao = SessaoVotacao.abrir(UUID.randomUUID(), inicio, fim);

        Instant agora = fim.plusSeconds(1);

        assertThat(sessao.estaAberta(agora)).isFalse();
    }

    @Test
    void deveEstarFechadaExatamenteNoMomentoDoFim() {
        Instant inicio = Instant.parse("2026-08-19T23:00:00Z");
        Instant fim = inicio.plusSeconds(60);

        SessaoVotacao sessao = SessaoVotacao.abrir(UUID.randomUUID(), inicio, fim);

        assertThat(sessao.estaAberta(fim)).isFalse();
    }

    @Test
    void deveEstarAbertaExatamenteNoMomentoDoInicio() {
        Instant inicio = Instant.parse("2026-08-19T23:00:00Z");
        Instant fim = inicio.plusSeconds(60);

        SessaoVotacao sessao = SessaoVotacao.abrir(UUID.randomUUID(), inicio, fim);

        assertThat(sessao.estaAberta(inicio)).isTrue();
    }

    @Test
    void naoDevePermitirPautaIdNulo() {
        Instant inicio = Instant.parse("2026-08-19T23:00:00Z");
        Instant fim = inicio.plusSeconds(60);

        assertThatThrownBy(() -> SessaoVotacao.abrir(null, inicio, fim))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("PautaId é obrigatório");
    }

    @Test
    void naoDevePermitirInicioNulo() {
        Instant fim = Instant.parse("2026-08-19T23:01:00Z");

        assertThatThrownBy(() -> SessaoVotacao.abrir(PAUTA_ID, null, fim))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Inicio é obrigatório");
    }

    @Test
    void naoDevePermitirFimNulo() {
        Instant inicio = Instant.parse("2026-08-19T23:00:00Z");

        assertThatThrownBy(() -> SessaoVotacao.abrir(PAUTA_ID, inicio, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Fim é obrigatório");
    }

    @Test
    void naoDevePermitirFimAnteriorAoInicio() {
        Instant inicio = Instant.parse("2026-08-19T23:00:00Z");
        Instant fim = inicio.minusSeconds(1);

        assertThatThrownBy(() -> SessaoVotacao.abrir(PAUTA_ID, inicio, fim)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Fim deve ser posterior ao início");
    }

    @Test
    void naoDevePermitirFimIgualAoInicio() {
        Instant inicio = Instant.parse("2026-08-19T23:00:00Z");

        assertThatThrownBy(() -> SessaoVotacao.abrir(PAUTA_ID, inicio, inicio)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Fim deve ser posterior ao início");
    }

    @Test
    void naoDevePermitirAgoraNulo() {
        Instant inicio = Instant.parse("2026-08-19T23:00:00Z");
        Instant fim = inicio.plusSeconds(60);

        SessaoVotacao sessao = SessaoVotacao.abrir(UUID.randomUUID(), inicio, fim);

        assertThatThrownBy(() -> sessao.estaAberta(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Agora é obrigatório");
    }

    @Test
    void deveGerarUmIdAoCriarSessao() {
        Instant inicio = Instant.parse("2026-08-19T23:00:00Z");
        Instant fim = inicio.plusSeconds(60);

        SessaoVotacao sessao = SessaoVotacao.abrir(UUID.randomUUID(), inicio, fim);

        assertThat(sessao.id()).isNotNull();
    }
}