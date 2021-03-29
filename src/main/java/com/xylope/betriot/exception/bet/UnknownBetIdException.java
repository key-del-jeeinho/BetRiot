package com.xylope.betriot.exception.bet;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UnknownBetIdException extends RuntimeException {
    public UnknownBetIdException(String msg) {
        super(msg);
    }

    public UnknownBetIdException(IndexOutOfBoundsException cause) {
        super(cause);
    }

    public UnknownBetIdException(String msg, IndexOutOfBoundsException cause) {
        super(msg, cause);
    }
}
