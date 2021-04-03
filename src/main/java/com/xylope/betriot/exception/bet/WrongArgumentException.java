package com.xylope.betriot.exception.bet;

import lombok.Getter;

public class WrongArgumentException extends RuntimeException {
    @Getter
    private final int argumentIdx;

    public WrongArgumentException(String message, Throwable cause, int argumentIdx) {
        super(message, cause);
        this.argumentIdx = argumentIdx;
    }

    public WrongArgumentException(Throwable cause, int argumentIdx) {
        super(cause);
        this.argumentIdx = argumentIdx;
    }
}
