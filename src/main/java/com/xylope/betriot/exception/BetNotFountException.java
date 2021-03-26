package com.xylope.betriot.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BetNotFountException extends DataNotFoundException {
    public BetNotFountException(String s) {
        super(s);
    }
}
