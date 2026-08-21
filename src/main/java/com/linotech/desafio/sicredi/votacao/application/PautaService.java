package com.linotech.desafio.sicredi.votacao.application;

import com.linotech.desafio.sicredi.votacao.application.repository.PautaRepository;
import com.linotech.desafio.sicredi.votacao.domain.Pauta;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PautaService {

    private final PautaRepository pautaRepository;

    public Pauta criar(String titulo) {
        log.info("m=criar, s=STARTED");
        Pauta pauta = new Pauta(titulo);
        Pauta salva = pautaRepository.save(pauta);
        log.info("m=criar, s=COMPLETED, id={}, título='{}'", salva.id(), salva.titulo());
        return salva;
    }
}
