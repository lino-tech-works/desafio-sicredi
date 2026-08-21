package com.linotech.desafio.sicredi.votacao.domain;

import java.util.UUID;

public record Pauta(UUID id, String titulo) {

    public Pauta(String titulo) {
        this(UUID.randomUUID(), titulo);
    }

    public static Pauta criar(UUID id, String titulo) {
        return new Pauta(id, titulo);
    }
}
