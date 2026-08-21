package com.linotech.desafio.sicredi.votacao.infraestructure.persistence.repository;

import com.linotech.desafio.sicredi.votacao.domain.Pauta;
import com.linotech.desafio.sicredi.votacao.infraestructure.persistence.repository.adapter.PautaRepositoryAdapter;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class PautaRepositoryAdapterTest {

    @Autowired
    private PautaRepositoryAdapter adapter;

    @Test
    void devePersistirEPesquisarPauta() {
        Pauta pauta = new Pauta("Aprovação do orçamento");

        UUID id = pauta.id();

        Pauta salva = adapter.save(pauta);

        assertThat(salva.id()).isEqualTo(id);

        Pauta encontrada = adapter.findById(id).orElseThrow();

        assertThat(encontrada.id()).isEqualTo(id);
        assertThat(encontrada.titulo()).isEqualTo("Aprovação do orçamento");
    }
}