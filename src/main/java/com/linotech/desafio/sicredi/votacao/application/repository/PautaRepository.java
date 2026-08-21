package com.linotech.desafio.sicredi.votacao.application.repository;

import com.linotech.desafio.sicredi.votacao.domain.Pauta;
import java.util.Optional;
import java.util.UUID;

public interface PautaRepository {

    Pauta save(Pauta pauta);

    Optional<Pauta> findById(UUID id);
}