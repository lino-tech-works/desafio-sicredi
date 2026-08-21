package com.linotech.desafio.sicredi.votacao.infraestructure.persistence.repository;

import com.linotech.desafio.sicredi.votacao.infraestructure.persistence.entity.PautaEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPautaRepository extends JpaRepository<PautaEntity, UUID> {
}