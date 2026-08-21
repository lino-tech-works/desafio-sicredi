package com.linotech.desafio.sicredi.votacao.infraestructure.persistence.repository.adapter;

import com.linotech.desafio.sicredi.votacao.application.repository.VotoRepository;
import com.linotech.desafio.sicredi.votacao.domain.ResultadoVotacao;
import com.linotech.desafio.sicredi.votacao.domain.Voto;
import com.linotech.desafio.sicredi.votacao.infraestructure.persistence.entity.VotoEntity;
import com.linotech.desafio.sicredi.votacao.infraestructure.persistence.mapper.VotoMapper;
import com.linotech.desafio.sicredi.votacao.infraestructure.persistence.repository.JpaVotoRepository;
import com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.response.ContagemVotosProjection;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class VotoRepositoryAdapter implements VotoRepository {

    private final JpaVotoRepository repository;
    private final VotoMapper mapper;

    @Override
    public boolean existsByPautaIdAndCpf(UUID pautaId, String cpf) {
        return repository.existsByPautaIdAndCpf(pautaId, cpf);
    }

    @Override
    public ResultadoVotacao contarPorPautaId(UUID pautaId) {
        ContagemVotosProjection projection = repository.contarPorPautaId(pautaId);
        return new ResultadoVotacao(pautaId, projection.getQuantidadeSim(), projection.getQuantidadeNao());
    }

    @Override
    public Voto salvar(Voto voto) {
        VotoEntity entity = mapper.toEntity(voto);
        VotoEntity saved = repository.save(entity);

        return mapper.toDomain(saved);
    }
}