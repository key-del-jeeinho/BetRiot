package com.xylope.betriot.layer.service.bet.reader;
import com.xylope.betriot.layer.service.bet.BetService;

@FunctionalInterface
public interface BetReader<T> {
    void read(BetService service, Action action, T input);
}
