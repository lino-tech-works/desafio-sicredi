package com.linotech.desafio.sicredi.votacao.domain;

import java.time.Instant;
import java.util.UUID;

public class SessaoVotacao {
    private final UUID id;
    private final UUID pautaId;
    private final Instant inicio;
    private final Instant fim;

    public SessaoVotacao(UUID id, UUID pautaId, Instant inicio, Instant fim) {
        validar(pautaId, inicio, fim);
        this.id = id;
        this.pautaId = pautaId;
        this.inicio = inicio;
        this.fim = fim;
    }

    public UUID id() {
        return id;
    }

    public UUID pautaId() {
        return pautaId;
    }

    public Instant inicio() {
        return inicio;
    }

    public Instant fim() {
        return fim;
    }

    public static SessaoVotacao abrir(UUID pautaId, Instant inicio, Instant fim) {
        return new SessaoVotacao(UUID.randomUUID(), pautaId, inicio, fim);
    }

    public static SessaoVotacao reconstituir(UUID id, UUID pautaId, Instant inicio, Instant fim) {
        if (id == null) {
            throw new IllegalArgumentException("Id é obrigatório");
        }
        return new SessaoVotacao(id, pautaId, inicio, fim);
    }

    private static void validar(UUID pautaId, Instant inicio, Instant fim) {
        if (pautaId == null) {
            throw new IllegalArgumentException("PautaId é obrigatório");
        }
        if (inicio == null) {
            throw new IllegalArgumentException("Inicio é obrigatório");
        }
        if (fim == null) {
            throw new IllegalArgumentException("Fim é obrigatório");
        }
        if (!fim.isAfter(inicio)) {
            throw new IllegalArgumentException("Fim deve ser posterior ao início");
        }
    }

    public boolean estaAberta(Instant agora) {
        if (agora == null) {
            throw new IllegalArgumentException("Agora é obrigatório");
        }
        return !agora.isBefore(inicio) && agora.isBefore(fim);
    }
}
