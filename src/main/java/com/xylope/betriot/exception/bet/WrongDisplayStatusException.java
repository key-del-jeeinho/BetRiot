package com.xylope.betriot.exception.bet;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WrongDisplayStatusException extends RuntimeException {
    public WrongDisplayStatusException(String s) {
        super(s);
    }

    public WrongDisplayStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongDisplayStatusException(Throwable cause) {
        super(cause);
    }
}
