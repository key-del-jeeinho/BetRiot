package com.xylope.betriot.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class IllegalProcessException extends RuntimeException {
    public IllegalProcessException(String msg) {
        super(msg);
    }
}
