package com.xylope.betriot.layer.service.bet_v2.reader;
import com.xylope.betriot.layer.service.bet_v2.BetService;

@FunctionalInterface
public interface BetReader<T> {
    void read(BetService service, Action action, T input);
}
