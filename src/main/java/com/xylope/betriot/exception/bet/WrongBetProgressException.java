package com.xylope.betriot.exception.bet;

import com.xylope.betriot.layer.service.bet_v2.model.BetProgress;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WrongBetProgressException extends RuntimeException {
    @Getter
    private BetProgress rightProgress;
    public WrongBetProgressException(String s, BetProgress rightProgress) {
        super(s);
        this.rightProgress = rightProgress;
    }

    public WrongBetProgressException(Throwable cause, BetProgress rightProgress) {
        super(cause);
        this.rightProgress = rightProgress;
    }

    public WrongBetProgressException(String message, Throwable cause, BetProgress rightProgress) {
        super(message, cause);
        this.rightProgress = rightProgress;
    }

    public WrongBetProgressException(String message) {
        super(message);
    }

    public WrongBetProgressException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongBetProgressException(Throwable cause) {
        super(cause);
    }
}
