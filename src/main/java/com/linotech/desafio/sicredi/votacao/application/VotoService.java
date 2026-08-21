package com.linotech.desafio.sicredi.votacao.application;

import com.linotech.desafio.sicredi.votacao.application.repository.PautaRepository;
import com.linotech.desafio.sicredi.votacao.application.repository.SessaoVotacaoRepository;
import com.linotech.desafio.sicredi.votacao.application.repository.VotoRepository;
import com.linotech.desafio.sicredi.votacao.domain.ResultadoVotacao;
import com.linotech.desafio.sicredi.votacao.domain.SessaoVotacao;
import com.linotech.desafio.sicredi.votacao.domain.TipoVoto;
import com.linotech.desafio.sicredi.votacao.domain.Voto;
import com.linotech.desafio.sicredi.votacao.domain.exception.BusinessException;
import com.linotech.desafio.sicredi.votacao.domain.exception.NotFoundException;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VotoService {

    private final Clock clock;
    private final VotoRepository votoRepository;
    private final PautaRepository pautaRepository;
    private final SessaoVotacaoRepository sessaoVotacaoRepository;

    @Transactional
    public Voto votar(UUID pautaId, UUID sessaoVotacaoId, String cpf, TipoVoto tipo) {
        log.info("m=votar, s=STARTED, pautaId={}, sessaoVotacaoId={}, tipo={}", pautaId, sessaoVotacaoId, tipo);

        pautaRepository.findById(pautaId).orElseThrow(() -> new NotFoundException("pauta.nao-encontrada"));

        SessaoVotacao sessao = sessaoVotacaoRepository.findById(sessaoVotacaoId)
                .orElseThrow(() -> new NotFoundException("sessao.nao-encontrada"));

        validarSessaoPertenceAPauta(sessao, pautaId);

        Instant agora = Instant.now(clock);

        if (!sessao.estaAberta(agora)) {
            log.warn("Tentativa de voto em sessão encerrada. pautaId={}, sessaoVotacaoId={}", pautaId, sessaoVotacaoId);
            throw new BusinessException("sessao.fechada");
        }

        if (votoRepository.existsByPautaIdAndCpf(pautaId, cpf)) {
            log.warn("Tentativa de voto duplicado. pautaId={}, sessaoVotacaoId={}", pautaId, sessaoVotacaoId);
            throw new BusinessException("voto.duplicado");
        }

        Voto voto = Voto.registrar(pautaId, sessaoVotacaoId, cpf, tipo, agora);

        Voto salvo = votoRepository.salvar(voto);
        log.info("m=votar, s=COMPLETED, votoId={}, pautaId={}, sessaoVotacaoId={}, tipo={}", salvo.id(), pautaId, sessaoVotacaoId, tipo);
        return salvo;
    }

    @Transactional(readOnly = true)
    public ResultadoVotacao resultado(UUID pautaId) {
        log.info("m=resultado, s=STARTED, pautaId={}", pautaId);
        pautaRepository.findById(pautaId).orElseThrow(() -> new NotFoundException("pauta.nao-encontrada"));
        ResultadoVotacao resultado = votoRepository.contarPorPautaId(pautaId);
        log.info("m=resultado, s=COMPLETED, pautaId={}, sim={}, nao={}", pautaId, resultado.quantidadeSim(), resultado.quantidadeNao());
        return resultado;
    }

    private void validarSessaoPertenceAPauta(SessaoVotacao sessao, UUID pautaId) {
        if (!sessao.pautaId().equals(pautaId)) {
            log.warn("Sessão não pertence à pauta. sessaoId={}, pautaIdInformado={}, pautaIdDaSessao={}",
                    sessao.id(), pautaId, sessao.pautaId());
            throw new BusinessException("sessao.nao-pertence-pauta");
        }
    }
}
