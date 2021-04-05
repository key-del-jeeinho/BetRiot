package com.xylope.betriot.exception;

import javax.annotation.Nonnull;

@Nonnull
public class AlreadyInitializeValueException extends RuntimeException {
    public AlreadyInitializeValueException(String message) {
        super(message);
    }
}
