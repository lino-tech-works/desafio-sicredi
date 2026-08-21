package com.linotech.desafio.sicredi.votacao.domain;

import java.util.UUID;

public record ResultadoVotacao(
        UUID pautaId,
        long quantidadeSim,
        long quantidadeNao
) {
}