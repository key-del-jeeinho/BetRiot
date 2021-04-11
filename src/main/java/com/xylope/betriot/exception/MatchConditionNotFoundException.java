package com.xylope.betriot.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MatchConditionNotFoundException extends RuntimeException {
    public MatchConditionNotFoundException(String s) {
        super(s);
    }
}
