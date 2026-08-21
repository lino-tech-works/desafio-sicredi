package com.linotech.desafio.sicredi.votacao.infraestructure.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("/users")
public interface UserInfoClient {

    @GetExchange("/{cpf}")
    UserInfoResponse buscarPorCpf(@PathVariable String cpf);
}