package com.xylope.betriot.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException(String s) {
        super(s);
    }
}
