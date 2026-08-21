package com.linotech.desafio.sicredi.votacao.infraestructure.persistence.entity;

import com.linotech.desafio.sicredi.votacao.domain.TipoVoto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "votos", uniqueConstraints = {
        @UniqueConstraint(name = "uk_voto_pauta_cpf", columnNames = {"pauta_id", "cpf"})
},
  indexes = {
        @Index(name = "idx_voto_pauta_tipo", columnList = "pauta_id,tipo")
   }
)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class VotoEntity {

    @Id
    private UUID id;

    @Column(name = "pauta_id", nullable = false)
    private UUID pautaId;

    @Column(name = "sessao_votacao_id", nullable = false)
    private UUID sessaoVotacaoId;

    @Column(nullable = false, length = 11)
    private String cpf;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private TipoVoto tipo;

    @Column(nullable = false)
    private Instant criadoEm;

}