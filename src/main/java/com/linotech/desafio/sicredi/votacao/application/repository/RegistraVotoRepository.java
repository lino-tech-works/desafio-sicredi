package com.linotech.desafio.sicredi.votacao.application.repository;

import com.linotech.desafio.sicredi.votacao.domain.TipoVoto;
import com.linotech.desafio.sicredi.votacao.domain.Voto;
import java.util.UUID;

public interface RegistraVotoRepository {

    Voto executar(UUID pautaId, UUID sessaoVotacaoId, String cpf, TipoVoto tipo);
}