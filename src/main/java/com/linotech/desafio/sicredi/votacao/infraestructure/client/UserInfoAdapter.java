package com.linotech.desafio.sicredi.votacao.infraestructure.client;

import com.linotech.desafio.sicredi.votacao.application.client.ElegibilidadeVotoStatus;
import com.linotech.desafio.sicredi.votacao.application.client.VerificadorElegibilidadeVotoClient;
import com.linotech.desafio.sicredi.votacao.domain.exception.BusinessException;
import com.linotech.desafio.sicredi.votacao.infraestructure.client.exception.CpfNaoEncontradoException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserInfoAdapter implements VerificadorElegibilidadeVotoClient {

    private final UserInfoClient client;

    @Override
    @CircuitBreaker(name = "userInfo", fallbackMethod = "fallbackVerificar")
    public ElegibilidadeVotoStatus verificar(String cpf) {
        UserInfoResponse userInfo = client.buscarPorCpf(cpf);
        return ElegibilidadeVotoStatus.valueOf(userInfo.status());
    }

    private ElegibilidadeVotoStatus fallbackVerificar(CpfNaoEncontradoException ex) {
        throw ex;
    }

    private ElegibilidadeVotoStatus fallbackVerificar(Exception ex) {
        log.error("Serviço de elegibilidade indisponível.", ex);
        throw new BusinessException("elegibilidade.servico-indisponivel");
    }
}