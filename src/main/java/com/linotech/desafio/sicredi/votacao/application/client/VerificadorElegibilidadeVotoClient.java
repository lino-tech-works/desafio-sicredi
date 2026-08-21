package com.linotech.desafio.sicredi.votacao.application.client;

public interface VerificadorElegibilidadeVotoClient {

    ElegibilidadeVotoStatus verificar(String cpf);
}