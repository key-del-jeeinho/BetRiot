package com.xylope.betriot.exception.bet;

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
