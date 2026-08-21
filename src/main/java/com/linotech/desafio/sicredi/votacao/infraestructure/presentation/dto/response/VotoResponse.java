package com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.response;


import com.linotech.desafio.sicredi.votacao.domain.TipoVoto;
import java.time.Instant;
import java.util.UUID;

public record VotoResponse(
        UUID id,
        UUID pautaId,
        UUID sessaoVotacaoId,
        String cpf,
        TipoVoto tipo,
        Instant criadoEm
) {
}