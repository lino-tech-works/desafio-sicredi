package com.linotech.desafio.sicredi.votacao.infraestructure.client.exception;

import com.linotech.desafio.sicredi.votacao.domain.exception.NotFoundException;

public class CpfNaoEncontradoException extends NotFoundException {

    public CpfNaoEncontradoException() {
        super("cpf.nao-encontrado");
    }
}