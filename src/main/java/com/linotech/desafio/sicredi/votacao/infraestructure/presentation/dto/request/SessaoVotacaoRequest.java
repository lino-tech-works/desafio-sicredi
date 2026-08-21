package com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.request;

import jakarta.validation.constraints.Positive;

public record SessaoVotacaoRequest(
        @Positive
        Long duracaoEmMinutos
) {
}