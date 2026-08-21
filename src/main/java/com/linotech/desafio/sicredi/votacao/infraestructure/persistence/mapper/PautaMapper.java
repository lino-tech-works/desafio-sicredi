package com.linotech.desafio.sicredi.votacao.infraestructure.persistence.mapper;

import com.linotech.desafio.sicredi.votacao.domain.Pauta;
import com.linotech.desafio.sicredi.votacao.infraestructure.persistence.entity.PautaEntity;
import com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.response.PautaResponse;
import org.mapstruct.Mapper;

@Mapper
public interface PautaMapper {

    Pauta toDomain(PautaEntity entity);

    PautaEntity toEntity(Pauta pauta);

    PautaResponse toResponse(Pauta pauta);
}
