package com.xylope.betriot.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WrongRegisterProgressException extends Exception {
    public WrongRegisterProgressException(String msg) {
        super(msg);
    }
}
