package com.xylope.betriot.layer.service.bet_v2.controller;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BetAlreadyCreatedException extends RuntimeException {
    public BetAlreadyCreatedException(String message) {
        super(message);
    }

    public BetAlreadyCreatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public BetAlreadyCreatedException(Throwable cause) {
        super(cause);
    }
}
