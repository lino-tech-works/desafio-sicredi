package com.linotech.desafio.sicredi.votacao.application;

import com.linotech.desafio.sicredi.votacao.application.repository.PautaRepository;
import com.linotech.desafio.sicredi.votacao.domain.Pauta;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;

    @Test
    void deveCriarEPersistirUmaPauta() {

        UUID id = UUID.randomUUID();

        when(pautaRepository.save(any(Pauta.class))).thenAnswer(invocation -> {
                    Pauta pauta = invocation.getArgument(0);
                    return new Pauta(id, pauta.titulo());
        });

        PautaService service = new PautaService(pautaRepository);

        Pauta pauta = service.criar("Aprovação do orçamento");

        assertThat(pauta.id()).isEqualTo(id);
        assertThat(pauta.titulo()).isEqualTo("Aprovação do orçamento");

        verify(pautaRepository).save(any(Pauta.class));
    }
}