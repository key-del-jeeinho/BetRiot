package com.xylope.betriot.exception;

public class CommandException extends RuntimeException {
    public CommandException(String reason) {
        super(reason);
    }
}
