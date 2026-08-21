package com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.response;

import java.util.UUID;

public record ResultadoVotacaoResponse(UUID pautaId, long quantidadeSim, long quantidadeNao){
}