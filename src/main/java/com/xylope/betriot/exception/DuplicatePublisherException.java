package com.xylope.betriot.exception;

import com.xylope.betriot.layer.service.bet.Bet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class DuplicatePublisherException extends RuntimeException {
    @Getter
    private Bet bet;
}
