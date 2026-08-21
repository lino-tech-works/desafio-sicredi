package com.linotech.desafio.sicredi.votacao.infraestructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "sessao_votacao")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class SessaoVotacaoEntity {

    @Id
    private UUID id;

    @Column(name = "pauta_id", nullable = false)
    private UUID pautaId;

    @JdbcTypeCode(SqlTypes.TIMESTAMP_WITH_TIMEZONE)
    @Column(name = "inicio", nullable = false)
    private Instant inicio;

    @JdbcTypeCode(SqlTypes.TIMESTAMP_WITH_TIMEZONE)
    @Column(name = "fim", nullable = false)
    private Instant fim;

}
