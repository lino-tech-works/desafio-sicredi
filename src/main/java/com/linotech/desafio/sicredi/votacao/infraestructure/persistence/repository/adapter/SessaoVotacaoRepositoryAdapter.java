package com.linotech.desafio.sicredi.votacao.infraestructure.persistence.repository.adapter;

import com.linotech.desafio.sicredi.votacao.application.repository.SessaoVotacaoRepository;
import com.linotech.desafio.sicredi.votacao.domain.SessaoVotacao;
import com.linotech.desafio.sicredi.votacao.infraestructure.persistence.entity.SessaoVotacaoEntity;
import com.linotech.desafio.sicredi.votacao.infraestructure.persistence.mapper.SessaoVotacaoMapper;
import com.linotech.desafio.sicredi.votacao.infraestructure.persistence.repository.JpaSessaoVotacaoRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SessaoVotacaoRepositoryAdapter implements SessaoVotacaoRepository {

    private final JpaSessaoVotacaoRepository repository;
    private final SessaoVotacaoMapper mapper;

    @Override
    public SessaoVotacao save(SessaoVotacao sessao) {
        SessaoVotacaoEntity entity = mapper.toEntity(sessao);
        SessaoVotacaoEntity saved = repository.save(entity);

        return mapper.toDomain(saved);
    }

    @Override
    public Optional<SessaoVotacao> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

}