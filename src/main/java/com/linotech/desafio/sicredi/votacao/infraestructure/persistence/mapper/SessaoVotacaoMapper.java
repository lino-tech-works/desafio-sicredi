package com.linotech.desafio.sicredi.votacao.infraestructure.persistence.mapper;

import com.linotech.desafio.sicredi.votacao.domain.SessaoVotacao;
import com.linotech.desafio.sicredi.votacao.infraestructure.persistence.entity.SessaoVotacaoEntity;
import com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.response.SessaoVotacaoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SessaoVotacaoMapper {

    @Mapping(target = "id", expression = "java(sessao.id())")
    @Mapping(target = "pautaId", expression = "java(sessao.pautaId())")
    @Mapping(target = "inicio", expression = "java(sessao.inicio())")
    @Mapping(target = "fim", expression = "java(sessao.fim())")
    SessaoVotacaoEntity toEntity(SessaoVotacao sessao);

    default SessaoVotacao toDomain(SessaoVotacaoEntity entity) {
        return SessaoVotacao.reconstituir(entity.getId(), entity.getPautaId(), entity.getInicio(), entity.getFim());
    }

    @Mapping(target = "id", expression = "java(sessaoVotacao.id())")
    @Mapping(target = "pautaId", expression = "java(sessaoVotacao.pautaId())")
    @Mapping(target = "inicio", expression = "java(sessaoVotacao.inicio())")
    @Mapping(target = "fim", expression = "java(sessaoVotacao.fim())")
    SessaoVotacaoResponse toResponse(SessaoVotacao sessaoVotacao);
}
