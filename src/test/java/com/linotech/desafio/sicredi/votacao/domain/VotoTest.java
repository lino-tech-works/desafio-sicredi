package com.linotech.desafio.sicredi.votacao.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VotoTest {

    @Test
    void deveRegistrarVoto() {
        UUID pautaId = UUID.randomUUID();
        UUID sessaoId = UUID.randomUUID();
        String cpf = "12345678900";
        Instant criadoEm = Instant.parse("2026-08-19T23:00:00Z");

        Voto voto = Voto.registrar(pautaId, sessaoId, cpf, TipoVoto.SIM, criadoEm);

        assertThat(voto.id()).isNotNull();
        assertThat(voto.sessaoVotacaoId()).isEqualTo(sessaoId);
        assertThat(voto.cpf()).isEqualTo(cpf);
        assertThat(voto.tipo()).isEqualTo(TipoVoto.SIM);
        assertThat(voto.criadoEm()).isEqualTo(criadoEm);
    }

    @Test
    void deveGerarIdsDiferentesParaVotosDiferentes() {
        UUID pautaId = UUID.randomUUID();
        UUID sessaoId = UUID.randomUUID();
        Instant criadoEm = Instant.parse("2026-08-19T23:00:00Z");

        Voto primeiro = Voto.registrar(pautaId, sessaoId,"12345678900", TipoVoto.SIM, criadoEm);
        Voto segundo = Voto.registrar(pautaId, sessaoId,"98765432100", TipoVoto.NAO, criadoEm);

        assertThat(primeiro.id()).isNotEqualTo(segundo.id());
    }

    @Test
    void naoDevePermitirSessaoNula() {
        Instant agora = Instant.now();
        assertThatThrownBy(() ->
                Voto.registrar(null,null, "12345678900", TipoVoto.SIM, agora))
                .isInstanceOf(NullPointerException.class);
    }
}