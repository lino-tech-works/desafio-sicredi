package com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.response;

import lombok.Builder;

@Builder
public record PayloadErrorResponse(
        String errorCode,
        String message
) {
}