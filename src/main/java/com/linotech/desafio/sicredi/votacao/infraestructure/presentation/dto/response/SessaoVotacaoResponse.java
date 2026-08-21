package com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.response;

import java.time.Instant;
import java.util.UUID;

public record SessaoVotacaoResponse(UUID id, UUID pautaId, Instant inicio, Instant fim) {}