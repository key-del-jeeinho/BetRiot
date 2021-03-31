package com.xylope.betriot.exception.bet;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MatchNotStartException extends RuntimeException {
    public MatchNotStartException(String message) {
        super(message);
    }

    public MatchNotStartException(String message, Throwable cause) {
        super(message, cause);
    }

    public MatchNotStartException(Throwable cause) {
        super(cause);
    }
}
