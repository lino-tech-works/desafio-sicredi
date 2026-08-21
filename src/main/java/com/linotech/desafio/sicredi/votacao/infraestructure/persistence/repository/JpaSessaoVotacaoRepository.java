package com.linotech.desafio.sicredi.votacao.infraestructure.persistence.repository;

import com.linotech.desafio.sicredi.votacao.infraestructure.persistence.entity.SessaoVotacaoEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSessaoVotacaoRepository extends JpaRepository<SessaoVotacaoEntity, UUID> {
}