package com.xylope.betriot.exception.bet;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class
UnknownBetIdException extends RuntimeException {
    public UnknownBetIdException(String msg) {
        super(msg);
    }

    public UnknownBetIdException(Throwable cause) {
        super(cause);
    }

    public UnknownBetIdException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
