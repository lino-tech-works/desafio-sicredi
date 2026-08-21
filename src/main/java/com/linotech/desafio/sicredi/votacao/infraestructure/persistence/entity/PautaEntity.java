package com.linotech.desafio.sicredi.votacao.infraestructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pautas")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PautaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String titulo;

}
