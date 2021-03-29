package com.xylope.betriot.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BetNotFoundException extends DataNotFoundException {
    public BetNotFoundException(String s) {
        super(s);
    }
}
