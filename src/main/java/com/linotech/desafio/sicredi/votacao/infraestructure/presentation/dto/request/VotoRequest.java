package com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.request;

import com.linotech.desafio.sicredi.votacao.domain.TipoVoto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record VotoRequest(

        @NotBlank(message = "CPF é obrigatório.")
        @Pattern(
                regexp = "\\d{11}",
                message = "CPF deve conter 11 dígitos."
        )
        String cpf,

        @NotNull(message = "Tipo de voto é obrigatório.")
        TipoVoto tipo
) {
}