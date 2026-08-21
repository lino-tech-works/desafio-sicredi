package com.linotech.desafio.sicredi.votacao.application;

import com.linotech.desafio.sicredi.votacao.application.repository.PautaRepository;
import com.linotech.desafio.sicredi.votacao.application.repository.SessaoVotacaoRepository;
import com.linotech.desafio.sicredi.votacao.domain.SessaoVotacao;
import com.linotech.desafio.sicredi.votacao.domain.exception.NotFoundException;
import com.linotech.desafio.sicredi.votacao.infraestructure.presentation.dto.request.SessaoVotacaoRequest;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ResourceBundle;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessaoVotacaoService {

    private static final Duration DURACAO_PADRAO = Duration.ofMinutes(1);
    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("messages");

    private final PautaRepository pautaRepository;
    private final SessaoVotacaoRepository sessaoVotacaoRepository;
    private final Clock clock;

    public SessaoVotacao abrir(UUID pautaId, SessaoVotacaoRequest request) {
        log.info("m=abrir, s=STARTED, pautaId={}", pautaId);
        validarPautaId(pautaId);

        pautaRepository.findById(pautaId).orElseThrow(() -> new NotFoundException("pauta.nao-encontrada"));

        Duration duracao = obterDuracao(request);

        Instant inicio = Instant.now(clock);
        Instant fim = inicio.plus(duracao);

        SessaoVotacao sessao = SessaoVotacao.abrir(pautaId, inicio, fim);

        SessaoVotacao salva = sessaoVotacaoRepository.save(sessao);
        log.info("m=abrir, s=COMPLETED, id={}, pautaId={}, duração={}min, início={}, fim={}",
                salva.id(), pautaId, duracao.toMinutes(), inicio, fim);
        return salva;
    }

    private Duration obterDuracao(SessaoVotacaoRequest request) {
        if (request == null || request.duracaoEmMinutos() == null) {
            log.debug("Duração não informada. Utilizando duração padrão de {} minuto(s)", DURACAO_PADRAO.toMinutes());
            return DURACAO_PADRAO;
        }

        long duracaoEmMinutos = request.duracaoEmMinutos();

        if (duracaoEmMinutos <= 0) {
            log.warn("Duração inválida informada: {} minutos", duracaoEmMinutos);
            throw new IllegalArgumentException(MESSAGES.getString("sessao.duracao.invalida"));
        }

        return Duration.ofMinutes(duracaoEmMinutos);
    }

    private void validarPautaId(UUID pautaId) {
        if (pautaId == null) {
            throw new IllegalArgumentException("pauta.id.requerido");
        }
    }
}
