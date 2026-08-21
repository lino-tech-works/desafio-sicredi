package com.linotech.desafio.sicredi.votacao.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Voto {
    private final UUID id;
    private final UUID pautaId;
    private final UUID sessaoVotacaoId;
    private final String cpf;
    private final TipoVoto tipo;
    private final Instant criadoEm;

    private Voto(UUID id, UUID pautaId, UUID sessaoVotacaoId, String cpf, TipoVoto tipo, Instant criadoEm) {
        this.id = id;
        this.pautaId = pautaId;
        this.sessaoVotacaoId = sessaoVotacaoId;
        this.cpf = cpf;
        this.tipo = tipo;
        this.criadoEm = criadoEm;
    }

    public UUID id() {
        return id;
    }

    public UUID pautaId() {
        return pautaId;
    }

    public UUID sessaoVotacaoId() {
        return sessaoVotacaoId;
    }

    public String cpf() {
        return cpf;
    }

    public TipoVoto tipo() {
        return tipo;
    }

    public Instant criadoEm() {
        return criadoEm;
    }

    public static Voto registrar(UUID pautaId, UUID sessaoVotacaoId, String cpf, TipoVoto tipo, Instant criadoEm) {
        Objects.requireNonNull(pautaId, "pautaId não pode ser nulo");
        Objects.requireNonNull(sessaoVotacaoId, "sessaoVotacaoId não pode ser nulo");
        Objects.requireNonNull(cpf, "cpf não pode ser nulo");
        Objects.requireNonNull(tipo, "tipo não pode ser nulo");
        Objects.requireNonNull(criadoEm, "criadoEm não pode ser nulo");
        return new Voto(UUID.randomUUID(), pautaId, sessaoVotacaoId, cpf, tipo, criadoEm);
    }

    public static Voto reconstituir(UUID id, UUID pautaId, UUID sessaoVotacaoId, String cpf, TipoVoto tipo, Instant criadoEm) {
        return new Voto(id, pautaId, sessaoVotacaoId, cpf, tipo, criadoEm);
    }
}
