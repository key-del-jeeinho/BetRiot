package com.xylope.betriot.exception.bet;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WrongBetProgressException extends RuntimeException {
    public WrongBetProgressException(String s) {
        super(s);
    }

    public WrongBetProgressException(Throwable cause) {
        super(cause);
    }

    public WrongBetProgressException(String message, Throwable cause) {
        super(message, cause);
    }
}
