package com.xylope.betriot.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ZeroChampionMasteryPointException extends RuntimeException {
    public ZeroChampionMasteryPointException(String s) {
        super(s);
    }
}
