package com.linotech.desafio.sicredi.votacao.application;

import com.linotech.desafio.sicredi.votacao.application.repository.RegistraVotoRepository;
import com.linotech.desafio.sicredi.votacao.domain.TipoVoto;
import com.linotech.desafio.sicredi.votacao.domain.Voto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrarVotoService {

    private final RegistraVotoRepository repository;

    public Voto registrar(UUID pautaId, UUID sessaoVotacaoId, String cpf, TipoVoto tipo) {
        log.info("m=registrar, s=STARTED, pautaId={}, sessaoVotacaoId={}, tipo={}", pautaId, sessaoVotacaoId, tipo);
        Voto voto = repository.executar(pautaId, sessaoVotacaoId, cpf, tipo);
        log.info("m=registrar, s=COMPLETED, pautaId={}, sessaoVotacaoId={}, tipo={}", pautaId, sessaoVotacaoId, tipo);
        return voto;
    }

}
