package com.linotech.desafio.sicredi.votacao.infraestructure.persistence.repository;

import com.linotech.desafio.sicredi.votacao.infraestructure.persistence.entity.VotoEntity;
import com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.response.ContagemVotosProjection;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaVotoRepository extends JpaRepository<VotoEntity, UUID> {

    boolean existsByPautaIdAndCpf(UUID pautaId, String cpf);

    @Query("""
        SELECT
            COALESCE(SUM(CASE WHEN v.tipo = 'SIM' THEN 1 ELSE 0 END), 0) AS quantidadeSim,
            COALESCE(SUM(CASE WHEN v.tipo = 'NAO' THEN 1 ELSE 0 END), 0) AS quantidadeNao
        FROM VotoEntity v
        WHERE v.pautaId = :pautaId
        """)
    ContagemVotosProjection contarPorPautaId(@Param("pautaId") UUID pautaId);
}