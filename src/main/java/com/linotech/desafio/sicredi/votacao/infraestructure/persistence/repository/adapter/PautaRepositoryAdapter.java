package com.linotech.desafio.sicredi.votacao.infraestructure.persistence.repository.adapter;

import com.linotech.desafio.sicredi.votacao.application.repository.PautaRepository;
import com.linotech.desafio.sicredi.votacao.domain.Pauta;
import com.linotech.desafio.sicredi.votacao.infraestructure.persistence.entity.PautaEntity;
import com.linotech.desafio.sicredi.votacao.infraestructure.persistence.mapper.PautaMapper;
import com.linotech.desafio.sicredi.votacao.infraestructure.persistence.repository.JpaPautaRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PautaRepositoryAdapter implements PautaRepository {

    private final JpaPautaRepository repository;
    private final PautaMapper mapper;

    @Override
    public Pauta save(Pauta pauta) {

        PautaEntity entity = mapper.toEntity(pauta);

        PautaEntity savedEntity = repository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Pauta> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }
}