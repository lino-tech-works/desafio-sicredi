package com.linotech.desafio.sicredi.votacao.application.repository;

import com.linotech.desafio.sicredi.votacao.domain.SessaoVotacao;
import java.util.Optional;
import java.util.UUID;

public interface SessaoVotacaoRepository {

    SessaoVotacao save(SessaoVotacao sessao);

    Optional<SessaoVotacao> findById(UUID id);

}