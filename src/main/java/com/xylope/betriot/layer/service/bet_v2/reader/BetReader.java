package com.xylope.betriot.layer.service.bet_v2.reader;

@FunctionalInterface
public interface BetReader<T> {
    void read(T input);
}
