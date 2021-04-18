package com.xylope.betriot.exception.user;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WrongAccountProgressException extends RuntimeException {
    public WrongAccountProgressException(RuntimeException e) {
        super(e);
    }
}
