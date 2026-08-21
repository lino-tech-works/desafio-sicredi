package com.linotech.desafio.sicredi.votacao.application.repository;

import com.linotech.desafio.sicredi.votacao.domain.ResultadoVotacao;
import com.linotech.desafio.sicredi.votacao.domain.Voto;
import java.util.UUID;

public interface VotoRepository {

    boolean existsByPautaIdAndCpf(UUID pautaId, String cpf);

    ResultadoVotacao contarPorPautaId(UUID pautaId);

    Voto salvar(Voto voto);
}