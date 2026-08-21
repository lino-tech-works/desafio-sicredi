package com.linotech.desafio.sicredi.votacao.infraestructure.persistence.mapper;

import com.linotech.desafio.sicredi.votacao.domain.ResultadoVotacao;
import com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.response.ResultadoVotacaoResponse;
import org.mapstruct.Mapper;

@Mapper
public interface ResultadoVotacaoMapper {

    ResultadoVotacaoResponse toResponse(ResultadoVotacao resultadoVotacao);
}
