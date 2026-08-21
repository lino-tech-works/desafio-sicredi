package com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PautaRequest(
        @NotBlank(message = "Título é obrigatório") String titulo) {
}