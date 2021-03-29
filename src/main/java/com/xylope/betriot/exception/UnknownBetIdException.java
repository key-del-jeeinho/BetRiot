package com.xylope.betriot.exception;

import lombok.Getter;

public class UnknownBetIdException extends RuntimeException {
    @Getter
    private final int id;

    public UnknownBetIdException(Exception e, int id) {
        super(e);
        this.id = id;
    }
}
