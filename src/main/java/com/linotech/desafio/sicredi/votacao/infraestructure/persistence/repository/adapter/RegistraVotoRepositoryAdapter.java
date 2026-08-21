package com.linotech.desafio.sicredi.votacao.infraestructure.persistence.repository.adapter;

import com.linotech.desafio.sicredi.votacao.application.VotoService;
import com.linotech.desafio.sicredi.votacao.application.client.ElegibilidadeVotoStatus;
import com.linotech.desafio.sicredi.votacao.application.client.VerificadorElegibilidadeVotoClient;
import com.linotech.desafio.sicredi.votacao.application.repository.RegistraVotoRepository;
import com.linotech.desafio.sicredi.votacao.domain.TipoVoto;
import com.linotech.desafio.sicredi.votacao.domain.Voto;
import com.linotech.desafio.sicredi.votacao.domain.exception.BusinessException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RegistraVotoRepositoryAdapter implements RegistraVotoRepository {

    private final VerificadorElegibilidadeVotoClient elegibilidadeVotoClient;
    private final VotoService votoService;

    @Override
    public Voto executar(UUID pautaId, UUID sessaoVotacaoId, String cpf, TipoVoto tipo) {
        log.info("Iniciando fluxo de voto. pautaId={}, sessaoVotacaoId={}", pautaId, sessaoVotacaoId);

        ElegibilidadeVotoStatus status = elegibilidadeVotoClient.verificar(cpf);

        if (status == ElegibilidadeVotoStatus.UNABLE_TO_VOTE) {
            throw new BusinessException("associado.nao-elegivel");
        }

        return votoService.votar(pautaId, sessaoVotacaoId, cpf, tipo);
    }
}