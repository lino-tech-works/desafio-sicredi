package com.linotech.desafio.sicredi.votacao.infraestructure.persistence.repository;

import com.linotech.desafio.sicredi.votacao.application.repository.VotoRepository;
import com.linotech.desafio.sicredi.votacao.domain.ResultadoVotacao;
import com.linotech.desafio.sicredi.votacao.domain.TipoVoto;
import com.linotech.desafio.sicredi.votacao.domain.Voto;
import com.linotech.desafio.sicredi.votacao.infraestructure.persistence.entity.VotoEntity;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class VotoRepositoryAdapterTest {

    @Autowired
    private VotoRepository repository;

    @Autowired
    private JpaVotoRepository jpaVotoRepository;

    @Test
    void devePersistirESerRecuperado() {
        UUID pautaId = UUID.randomUUID();
        UUID sessaoVotacaoId = UUID.randomUUID();
        String cpf = "12345678900";
        Instant criadoEm = Instant.parse("2026-08-20T10:00:00Z");

        Voto voto = Voto.registrar(pautaId, sessaoVotacaoId, cpf, TipoVoto.SIM, criadoEm);

        repository.salvar(voto);

        jpaVotoRepository.flush();

        Optional<VotoEntity> encontrada = jpaVotoRepository.findById(voto.id());

        assertThat(encontrada).isPresent();

        VotoEntity entity = encontrada.get();

        assertThat(entity.getId()).isEqualTo(voto.id());
        assertThat(entity.getPautaId()).isEqualTo(pautaId);
        assertThat(entity.getSessaoVotacaoId()).isEqualTo(sessaoVotacaoId);
        assertThat(entity.getCpf()).isEqualTo(cpf);
        assertThat(entity.getTipo()).isEqualTo(TipoVoto.SIM);
    }

    @Test
    void deveIdentificarSeAssociadoJaVotouNaPauta() {
        UUID pautaId = UUID.randomUUID();
        UUID sessaoVotacaoId = UUID.randomUUID();
        String cpf = "12345678900";

        Voto voto = Voto.registrar(pautaId, sessaoVotacaoId, cpf, TipoVoto.SIM, Instant.parse("2026-08-20T10:00:00Z"));

        repository.salvar(voto);

        boolean resultado = repository.existsByPautaIdAndCpf(pautaId, cpf);

        assertThat(resultado).isTrue();
    }

    @Test
    void deveIdentificarSeAssociadoAindaNaoVotouNaPauta() {
        UUID pautaId = UUID.randomUUID();

        boolean resultado = repository.existsByPautaIdAndCpf(pautaId, "12345678900");

        assertThat(resultado).isFalse();
    }

    @Test
    void deveContabilizarVotosSimPorPauta() {
        UUID pautaId = UUID.randomUUID();

        repository.salvar(Voto.registrar(pautaId, UUID.randomUUID(), "12345678900", TipoVoto.SIM,
                Instant.parse("2026-08-20T10:00:00Z")));

        repository.salvar(Voto.registrar(pautaId, UUID.randomUUID(), "98765432100", TipoVoto.SIM,
                Instant.parse("2026-08-20T10:01:00Z")));

        repository.salvar(Voto.registrar(pautaId, UUID.randomUUID(), "11122233344", TipoVoto.NAO,
                Instant.parse("2026-08-20T10:02:00Z")));

        ResultadoVotacao resultadoVotacao = repository.contarPorPautaId(pautaId);

        assertThat(resultadoVotacao.quantidadeSim()).isEqualTo(2);
    }

    @Test
    void deveContabilizarVotosNaoPorPauta() {
        UUID pautaId = UUID.randomUUID();

        repository.salvar(Voto.registrar(pautaId, UUID.randomUUID(), "12345678900", TipoVoto.SIM,
                Instant.parse("2026-08-20T10:00:00Z")));

        repository.salvar(Voto.registrar(pautaId, UUID.randomUUID(), "98765432100", TipoVoto.NAO,
                Instant.parse("2026-08-20T10:01:00Z")));

        repository.salvar(Voto.registrar(pautaId, UUID.randomUUID(), "11122233344", TipoVoto.NAO,
                Instant.parse("2026-08-20T10:02:00Z")));

        ResultadoVotacao resultadoVotacao = repository.contarPorPautaId(pautaId);

        assertThat(resultadoVotacao.quantidadeNao()).isEqualTo(2);
    }

    @Test
    void naoDevePermitirDoisVotosDoMesmoAssociadoNaMesmaPauta() {
        UUID pautaId = UUID.randomUUID();
        String cpf = "12345678900";

        Voto primeiro = Voto.registrar(pautaId, UUID.randomUUID(), cpf, TipoVoto.SIM, Instant.parse("2026-08-20T10:00:00Z"));
        Voto segundo = Voto.registrar(pautaId, UUID.randomUUID(), cpf, TipoVoto.NAO, Instant.parse("2026-08-20T10:01:00Z"));

        repository.salvar(primeiro);

        assertThatThrownBy(() -> repository.salvar(segundo)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void devePermitirMesmoAssociadoVotarEmPautasDiferentes() {
        UUID primeiraPautaId = UUID.randomUUID();
        UUID segundaPautaId = UUID.randomUUID();
        String cpf = "12345678900";

        Voto primeiro = Voto.registrar(primeiraPautaId, UUID.randomUUID(), cpf, TipoVoto.SIM, Instant.parse("2026-08-20T10:00:00Z"));

        Voto segundo = Voto.registrar(
                segundaPautaId,
                UUID.randomUUID(),
                cpf,
                TipoVoto.NAO,
                Instant.parse("2026-08-20T10:01:00Z")
        );

        repository.salvar(primeiro);
        repository.salvar(segundo);

        jpaVotoRepository.flush();

        assertThat(
                jpaVotoRepository.findById(primeiro.id())
        ).isPresent();

        assertThat(
                jpaVotoRepository.findById(segundo.id())
        ).isPresent();
    }
}

