package com.linotech.desafio.sicredi.votacao.domain.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    private final String code;

    public NotFoundException(String code) {
        super(code);
        this.code = code;
    }

}