package com.linotech.desafio.sicredi.votacao.infraestructure.persistence.mapper;

import com.linotech.desafio.sicredi.votacao.domain.Voto;
import com.linotech.desafio.sicredi.votacao.infraestructure.persistence.entity.VotoEntity;
import com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.response.VotoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface VotoMapper {

    @Mapping(target = "id", expression = "java(voto.id())")
    @Mapping(target = "pautaId", expression = "java(voto.pautaId())")
    @Mapping(target = "sessaoVotacaoId", expression = "java(voto.sessaoVotacaoId())")
    @Mapping(target = "cpf", expression = "java(voto.cpf())")
    @Mapping(target = "tipo", expression = "java(voto.tipo())")
    @Mapping(target = "criadoEm", expression = "java(voto.criadoEm())")
    VotoEntity toEntity(Voto voto);

    default Voto toDomain(VotoEntity entity) {
        return Voto.reconstituir(entity.getId(), entity.getPautaId(), entity.getSessaoVotacaoId(),
                                 entity.getCpf(), entity.getTipo(), entity.getCriadoEm());
    }

    @Mapping(target = "id", expression = "java(voto.id())")
    @Mapping(target = "pautaId", expression = "java(voto.pautaId())")
    @Mapping(target = "sessaoVotacaoId", expression = "java(voto.sessaoVotacaoId())")
    @Mapping(target = "cpf", expression = "java(mascaraCpf(voto.cpf()))")
    @Mapping(target = "tipo", expression = "java(voto.tipo())")
    @Mapping(target = "criadoEm", expression = "java(voto.criadoEm())")
    VotoResponse toResponse(Voto voto);

    /**
     * Mascara o CPF no formato ***.XXX.XXX-**
     * Exemplo: "54863677782" → "***.636.777-**"
     */
    default String mascaraCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return cpf;
        }
        return "***." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-**";
    }
}
