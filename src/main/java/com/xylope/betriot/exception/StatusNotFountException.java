package com.xylope.betriot.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StatusNotFountException extends RuntimeException {
    public StatusNotFountException(String msg) {
        super(msg);
    }
}
